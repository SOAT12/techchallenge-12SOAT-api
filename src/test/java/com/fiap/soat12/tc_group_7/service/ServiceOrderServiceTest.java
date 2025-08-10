package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.entity.*;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.*;
import com.fiap.soat12.tc_group_7.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServiceOrderService Unit Tests")
class ServiceOrderServiceTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private VehicleServiceRepository serviceRepository;
    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ServiceOrderService serviceOrderService;

    private ServiceOrder serviceOrder;
    private Customer customer;
    private Vehicle vehicle;
    private Employee employee;
    private ServiceOrderRequestDTO requestDTO;
    private com.fiap.soat12.tc_group_7.entity.VehicleService vehicleService;
    private Stock stock;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        vehicle = new Vehicle();
        vehicle.setId(1L);

        employee = new Employee();
        employee.setId(1L);
        employee.setName("Test Employee");

        vehicleService = new com.fiap.soat12.tc_group_7.entity.VehicleService();
        vehicleService.setId(101L);
        vehicleService.setValue(new BigDecimal("100.00"));

        stock = new Stock();
        stock.setId(201L);
        stock.setValue(new BigDecimal("50.00"));

        serviceOrder = new ServiceOrder();
        serviceOrder.setId(1L);
        serviceOrder.setStatus(Status.OPENED);
        serviceOrder.setCustomer(customer);
        serviceOrder.setVehicle(vehicle);
        serviceOrder.setEmployee(employee);

        serviceOrder.setServices(Collections.emptySet());
        serviceOrder.setStockItems(Collections.emptySet());

        requestDTO = new ServiceOrderRequestDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setVehicleId(1L);
        requestDTO.setEmployeeId(1L);
    }

    @Nested
    @DisplayName("Create and Update Tests")
    class CreateUpdateTests {
        @Test
        void createServiceOrder_withValidData_shouldSucceed() {
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            ServiceOrderResponseDTO response = serviceOrderService.createServiceOrder(requestDTO);

            assertNotNull(response);
            assertEquals(Status.OPENED, response.getStatus());
            verify(serviceOrderRepository).save(any(ServiceOrder.class));
        }

        @Test
        void createServiceOrder_withServicesAndStock_shouldSucceed() {
            requestDTO.setServices(List.of(new ServiceOrderRequestDTO.VehicleServiceItemDTO(101L)));
            requestDTO.setStockItems(List.of(new ServiceOrderRequestDTO.StockItemDTO(201L, 2)));

            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
            when(serviceRepository.findById(101L)).thenReturn(Optional.of(vehicleService));
            when(stockRepository.findById(201L)).thenReturn(Optional.of(stock));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            ServiceOrderResponseDTO response = serviceOrderService.createServiceOrder(requestDTO);

            assertNotNull(response);
            verify(serviceRepository).findById(101L);
            verify(stockRepository).findById(201L);
        }

        @Test
        void updateOrder_whenOrderExists_shouldSucceed() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> response = serviceOrderService.updateOrder(1L, requestDTO);

            assertTrue(response.isPresent());
            assertEquals(Status.WAITING_FOR_APPROVAL, response.get().getStatus());
            verify(serviceOrderRepository).save(any(ServiceOrder.class));
        }

        @Test
        void updateOrder_whenOrderDoesNotExist_shouldThrowNotFoundException() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> serviceOrderService.updateOrder(1L, requestDTO));
        }
    }

    @Nested
    @DisplayName("Find and Delete Tests")
    class FindDeleteTests {
        @Test
        void findById_whenOrderExists_shouldReturnOrder() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            ServiceOrderResponseDTO response = serviceOrderService.findById(1L);
            assertNotNull(response);
            assertEquals(1L, response.getId());
        }

        @Test
        void findById_whenOrderDoesNotExist_shouldThrowNotFoundException() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> serviceOrderService.findById(1L));
        }

        @Test
        void deleteOrderLogically_whenOrderExists_shouldSucceed() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            serviceOrderService.deleteOrderLogically(1L);
            assertEquals(Status.CANCELED, serviceOrder.getStatus());
            verify(serviceOrderRepository).save(serviceOrder);
        }

        @Test
        void deleteOrderLogically_whenOrderNotFound_shouldThrowNotFoundException() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> serviceOrderService.deleteOrderLogically(1L));
        }
    }

    @Nested
    @DisplayName("State Transition Tests")
    class StateTransitionTests {
        @Test
        void approve_whenStatusIsWaitingForApproval_shouldSucceed() {
            serviceOrder.setStatus(Status.WAITING_FOR_APPROVAL);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> response = serviceOrderService.approve(1L, null);

            assertTrue(response.isPresent());
            assertEquals(Status.APPROVED, response.get().getStatus());
        }

        @Test
        void approve_whenStatusIsOpened_shouldThrowInvalidTransitionException() {
            serviceOrder.setStatus(Status.OPENED); // Invalid initial state
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            assertThrows(InvalidTransitionException.class, () -> serviceOrderService.approve(1L, null));
        }

        @Test
        void diagnose_whenEmployeeNotFound_shouldThrowNotFoundException() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> serviceOrderService.diagnose(1L, 99L));
        }
    }
}