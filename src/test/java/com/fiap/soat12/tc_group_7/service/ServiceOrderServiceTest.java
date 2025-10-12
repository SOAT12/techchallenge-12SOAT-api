package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.dto.stock.StockAvailabilityResponseDTO;
import com.fiap.soat12.tc_group_7.entity.*;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.*;
import com.fiap.soat12.tc_group_7.util.Status;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.*;

import static com.fiap.soat12.tc_group_7.service.ServiceOrderService.MECHANIC_DESCRIPTION;
import static com.fiap.soat12.tc_group_7.util.Status.APPROVED;
import static com.fiap.soat12.tc_group_7.util.Status.getStatusesForPendingOrders;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    @Mock
    private NotificationService notificationService;
    @Mock
    private StockService stockService;

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
            verify(notificationService).notifyMechanicAssignedToOS(serviceOrder, serviceOrder.getEmployee());
            verify(serviceOrderRepository).save(any(ServiceOrder.class));
        }
//
//        @Test
//        void createServiceOrder_withServicesAndStock_shouldSucceed() {
//            requestDTO.setServices(List.of(new ServiceOrderRequestDTO.VehicleServiceItemDTO(101L)));
//            requestDTO.setStockItems(List.of(new ServiceOrderRequestDTO.StockItemDTO(201L, 2)));
//
//            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
//            when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
//            when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
//            when(serviceRepository.findById(101L)).thenReturn(Optional.of(vehicleService));
//            when(stockRepository.findById(201L)).thenReturn(Optional.of(stock));
//            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);
//
//            ServiceOrderResponseDTO response = serviceOrderService.createServiceOrder(requestDTO);
//
//            assertNotNull(response);
//            verify(serviceRepository).findById(101L);
//            verify(stockRepository).findById(201L);
//            verify(notificationService).notifyMechanicAssignedToOS(serviceOrder, serviceOrder.getEmployee());
//        }

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
        void findAllOrders_whenOrderExists_shouldReturnAllOrders() {
            when(serviceOrderRepository.findAll()).thenReturn(List.of(serviceOrder));

            List<ServiceOrderResponseDTO> ordersList = serviceOrderService.findAllOrders();


            assertNotNull(serviceOrderService.findAllOrders());
            assertEquals(1, ordersList.size());
        }

        @Test
        void findByCustomerInfo_shouldSucceed() {
            String document = "0123456789";
            customer.setCpf(document);
            when(customerRepository.findByCpf(document)).thenReturn(Optional.of(customer));
            when(serviceOrderRepository.findByCustomerAndFinishedAtIsNull(customer)).thenReturn(List.of(serviceOrder));

            Optional<List<ServiceOrderResponseDTO>> byCustomerInfoList = serviceOrderService.findByCustomerInfo(document);

            verify(serviceOrderRepository).findByCustomerAndFinishedAtIsNull(any(Customer.class));
            assertNotNull(serviceOrderService.findByCustomerInfo(document));
            assertEquals(1, byCustomerInfoList.get().size());
            assertEquals(document, byCustomerInfoList.get().get(0).getCustomer().getDocument());
        }

        @Test
        void findByVehicleId_shouldSucceed() {
            String licensePlate = "ABC-1234";
            vehicle.setLicensePlate(licensePlate);

            when(vehicleRepository.findByLicensePlate(licensePlate)).thenReturn(Optional.of(vehicle));
            when(serviceOrderRepository.findByVehicleAndFinishedAtIsNull(vehicle)).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> byVehicleInfo = serviceOrderService.findByVehicleInfo(licensePlate);

            verify(serviceOrderRepository).findByVehicleAndFinishedAtIsNull(any(Vehicle.class));
            assertNotNull(serviceOrderService.findByVehicleInfo(licensePlate));
            assertEquals(licensePlate, byVehicleInfo.get().getVehicle().getLicensePlate());
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
//        @Test
//        void waitOnApprove_whenStatusIsInDiagnosis_shouldThrowInvalidTransitionException() throws MessagingException {
//            serviceOrder.setStatus(Status.IN_DIAGNOSIS);
//            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
//            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);
//
//            Optional<ServiceOrderResponseDTO> response = serviceOrderService.waitForApproval(1L);
//
//            assertTrue(response.isPresent());
//            assertEquals(Status.WAITING_FOR_APPROVAL, response.get().getStatus());
//        }

        @Test
        void approve_whenStatusIsWaitingForApproval_shouldSucceed() {
            serviceOrder.setStatus(Status.WAITING_FOR_APPROVAL);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> response = serviceOrderService.approve(1L, null);

            verify(notificationService).notifyMechanicOSApproved(serviceOrder, serviceOrder.getEmployee());
            assertTrue(response.isPresent());
            assertEquals(Status.APPROVED, response.get().getStatus());
        }

        @Test
        void approve_whenStatusIsOpened_shouldThrowInvalidTransitionException() {
            serviceOrder.setStatus(Status.OPENED);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            assertThrows(InvalidTransitionException.class, () -> serviceOrderService.approve(1L, null));
        }

        @Test
        void reject_whenStatusIsWaitingForApproval_shouldSucceed() {
            serviceOrder.setStatus(Status.WAITING_FOR_APPROVAL);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> response = serviceOrderService.reject(1L, "Muito caro");

            assertTrue(response.isPresent());
            assertEquals(Status.REJECTED, response.get().getStatus());
            assertEquals("Muito caro", response.get().getNotes());
        }

        @Test
        void reject_whenStatusIsOpened_shouldThrowInvalidTransitionException() {
            serviceOrder.setStatus(Status.OPENED);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            assertThrows(InvalidTransitionException.class, () -> serviceOrderService.reject(1L, null));
        }

        @Test
        void diagnose_whenEmployeeIsNotInformed_shouldSucceed() {
            employee.setActive(true);
            employee.setName("Mecânico 1");
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(anyString())).thenReturn(List.of(employee));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> response = serviceOrderService.diagnose(1L, null);

            assertTrue(response.isPresent());
            assertEquals(Status.IN_DIAGNOSIS, response.get().getStatus());
            verify(serviceOrderRepository).save(serviceOrder);
        }

        @Test
        void diagnose_whenEmployeeNotFound_shouldThrowNotFoundException() {
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(NotFoundException.class, () -> serviceOrderService.diagnose(1L, 99L));
        }

//        @Test
//        void finish_whenStatusIsInExecution_shouldSucceed() throws MessagingException {
//            serviceOrder.setStatus(Status.IN_EXECUTION);
//            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
//            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);
//
//            Optional<ServiceOrderResponseDTO> response = serviceOrderService.finish(1L);
//
//            assertTrue(response.isPresent());
//            assertEquals(Status.FINISHED, response.get().getStatus());
//        }

        @Test
        void deliver_whenStatusIsFinished_shouldSucceed() {
            serviceOrder.setStatus(Status.FINISHED);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(serviceOrderRepository.save(any(ServiceOrder.class))).thenReturn(serviceOrder);

            Optional<ServiceOrderResponseDTO> response = serviceOrderService.deliver(1L);

            assertTrue(response.isPresent());
            assertEquals(Status.DELIVERED, response.get().getStatus());
        }
    }

    @Nested
    @DisplayName("startOnExecution Flow Tests")
    class ExecutionFlowTests {
        @Test
        void startOrderExecution_whenStockIsAvailable_shouldSetStatusToExecute() {
            StockAvailabilityResponseDTO availability = new StockAvailabilityResponseDTO(true, Collections.emptyList());
            serviceOrder.setStatus(APPROVED);
            employee.setActive(true);
            employee.setName("Mecânico 1");
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(stockService.checkStockAvailability(serviceOrder)).thenReturn(availability);
            when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(anyString())).thenReturn(Collections.singletonList(employee));

            serviceOrderService.startOrderExecution(1L);

            verify(notificationService).notifyMechanicAssignedToOS(serviceOrder, employee);
            assertEquals(Status.IN_EXECUTION, serviceOrder.getStatus());
            verify(serviceOrderRepository).save(serviceOrder);
        }

        @Test
        void startOrderExecution_whenStockIsUnavailable_shouldSetStatusToWaitForStock() {
            StockAvailabilityResponseDTO availability = new StockAvailabilityResponseDTO(false, Collections.emptyList());
            serviceOrder.setStatus(APPROVED);
            when(serviceOrderRepository.findById(1L)).thenReturn(Optional.of(serviceOrder));
            when(stockService.checkStockAvailability(serviceOrder)).thenReturn(availability);

            serviceOrderService.startOrderExecution(1L);

            assertEquals(Status.WAITING_ON_STOCK, serviceOrder.getStatus());
            verify(serviceOrderRepository).save(serviceOrder);
        }
    }

    @Test
    void findMostAvailableEmployee_withAvailableEmployees() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Mecânico 1");
        employee1.setActive(true);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Mecânico 2");
        employee2.setActive(true);

        ServiceOrder order1 = new ServiceOrder();
        order1.setId(1L);
        order1.setEmployee(employee1);
        order1.setStatus(Status.OPENED);
        order1.setCreatedAt(new Date(System.currentTimeMillis() - 10000));

        ServiceOrder order2 = new ServiceOrder();
        order2.setId(2L);
        order2.setEmployee(employee2);
        order2.setStatus(Status.OPENED);
        order2.setCreatedAt(new Date(System.currentTimeMillis() - 5000));

        ServiceOrder order3 = new ServiceOrder();
        order3.setId(3L);
        order3.setEmployee(employee2);
        order3.setStatus(Status.IN_DIAGNOSIS);
        order3.setCreatedAt(new Date(System.currentTimeMillis() - 20000));

        List<Status> activeStatuses = getStatusesForPendingOrders();

        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MECHANIC_DESCRIPTION))
                .thenReturn(Arrays.asList(employee1, employee2));
        when(serviceOrderRepository.countByEmployeeAndStatusIn(employee1, activeStatuses))
                .thenReturn(1L);
        when(serviceOrderRepository.countByEmployeeAndStatusIn(employee2, activeStatuses))
                .thenReturn(2L);

        // Act
        Employee result = serviceOrderService.findMostAvailableEmployee();

        // Assert
        assertEquals(employee1, result);
    }

    @Test
    void findMostAvailableEmployee_withNoActiveEmployees() {
        // Arrange
        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MECHANIC_DESCRIPTION)).thenReturn(Collections.emptyList());

        // Act & Assert
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            serviceOrderService.findMostAvailableEmployee();
        });
        assertEquals("Nenhum mecânico disponível", exception.getMessage());
    }

    @Test
    void findMostAvailableEmployee_withEqualActiveOrders() {
        // Arrange
        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Mecânico 1");
        employee1.setActive(true);

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Mecânico 2");
        employee2.setActive(true);

        Employee employee3 = new Employee();
        employee3.setId(3L);
        employee3.setName("Mecânico 3");
        employee3.setActive(true);

        ServiceOrder order1 = new ServiceOrder();
        order1.setId(1L);
        order1.setEmployee(employee1);
        order1.setStatus(Status.OPENED);
        order1.setCreatedAt(new Date(System.currentTimeMillis() - 10000));

        ServiceOrder order2 = new ServiceOrder();
        order2.setId(2L);
        order2.setEmployee(employee2);
        order2.setStatus(Status.OPENED);
        order2.setCreatedAt(new Date(System.currentTimeMillis() - 5000));

        ServiceOrder order3 = new ServiceOrder();
        order3.setId(3L);
        order3.setEmployee(employee3);
        order3.setStatus(Status.IN_DIAGNOSIS);
        order3.setCreatedAt(new Date(System.currentTimeMillis() - 20000));

        List<Status> activeStatuses = getStatusesForPendingOrders();

        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MECHANIC_DESCRIPTION))
                .thenReturn(Arrays.asList(employee1, employee2, employee3));
        when(serviceOrderRepository.findByEmployeeAndStatusIn(employee1, activeStatuses))
                .thenReturn(Collections.singletonList(order1));
        when(serviceOrderRepository.findByEmployeeAndStatusIn(employee2, activeStatuses))
                .thenReturn(Collections.singletonList(order2));
        when(serviceOrderRepository.findByEmployeeAndStatusIn(employee3, activeStatuses))
                .thenReturn(Collections.singletonList(order3));

        // Act
        Employee result = serviceOrderService.findMostAvailableEmployee();

        // Assert
        assertEquals(employee3, result);
    }

    @Test
    public void getAverageExecutionTime_withSuccess() {
        Date now = new Date();
        Date oneHourAgo = new Date(now.getTime() - 3600_000);

        ServiceOrder order1 = new ServiceOrder();
        order1.setCreatedAt(oneHourAgo);
        order1.setFinishedAt(now);

        ServiceOrder order2 = new ServiceOrder();
        order2.setCreatedAt(oneHourAgo);
        order2.setFinishedAt(now);

        List<ServiceOrder> orders = Arrays.asList(order1, order2);

        when(serviceOrderRepository.findAll((Specification) any())).thenReturn(orders);

        AverageExecutionTimeResponseDTO dto = serviceOrderService.calculateAverageExecutionTime(null, null, null);

        // Como os dois têm duração de 1 hora, a média também é 1 hora = 3600000 ms
        assertEquals(1, dto.getAverageExecutionTimeHours());
        assertEquals("1 horas, 0 minutos", dto.getAverageExecutionTimeFormatted());
    }

}
