package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceOrderGatewayTest {

    @Mock
    private ServiceOrderRepository serviceOrderRepository;

    @InjectMocks
    private ServiceOrderGateway gateway;

    @Test
    void findAll_shouldReturnAllServiceOrders() {
        List<ServiceOrder> expected = List.of(mock(ServiceOrder.class), mock(ServiceOrder.class));
        when(serviceOrderRepository.findAll()).thenReturn(expected);

        List<ServiceOrder> result = gateway.findAll();

        assertEquals(expected, result);
        verify(serviceOrderRepository).findAll();
    }

    @Test
    void findAllWithFilters_shouldReturnFilteredList() {
        Date start = new Date();
        Date end = new Date();
        List<Long> serviceIds = List.of(1L, 2L);
        List<ServiceOrder> expected = List.of(mock(ServiceOrder.class));

        when(serviceOrderRepository.findAllWithFilters(start, end, serviceIds)).thenReturn(expected);

        List<ServiceOrder> result = gateway.findAllWithFilters(start, end, serviceIds);

        assertEquals(expected, result);
        verify(serviceOrderRepository).findAllWithFilters(start, end, serviceIds);
    }

    @Test
    void findById_shouldReturnOptional() {
        Long id = 1L;
        ServiceOrder order = mock(ServiceOrder.class);

        when(serviceOrderRepository.findById(id)).thenReturn(Optional.of(order));

        Optional<ServiceOrder> result = gateway.findById(id);

        assertTrue(result.isPresent());
        assertEquals(order, result.get());
        verify(serviceOrderRepository).findById(id);
    }

    @Test
    void countByEmployeeAndStatusIn_shouldReturnCount() {
        Employee employee = mock(Employee.class);
        List<Status> statuses = List.of(Status.OPENED, Status.APPROVED);
        Long expectedCount = 3L;

        when(serviceOrderRepository.countByEmployeeAndStatusIn(employee, statuses)).thenReturn(expectedCount);

        Long result = gateway.countByEmployeeAndStatusIn(employee, statuses);

        assertEquals(expectedCount, result);
        verify(serviceOrderRepository).countByEmployeeAndStatusIn(employee, statuses);
    }

    @Test
    void findByEmployeeAndStatusIn_shouldReturnList() {
        Employee employee = mock(Employee.class);
        List<Status> statuses = List.of(Status.IN_DIAGNOSIS, Status.IN_EXECUTION);
        List<ServiceOrder> expected = List.of(mock(ServiceOrder.class));

        when(serviceOrderRepository.findByEmployeeAndStatusIn(employee, statuses)).thenReturn(expected);

        List<ServiceOrder> result = gateway.findByEmployeeAndStatusIn(employee, statuses);

        assertEquals(expected, result);
        verify(serviceOrderRepository).findByEmployeeAndStatusIn(employee, statuses);
    }

    @Test
    void findByCustomerAndFinishedAtIsNull_shouldReturnOpenOrders() {
        Customer customer = mock(Customer.class);
        List<ServiceOrder> expected = List.of(mock(ServiceOrder.class));

        when(serviceOrderRepository.findByCustomerAndFinishedAtIsNull(customer)).thenReturn(expected);

        List<ServiceOrder> result = gateway.findByCustomerAndFinishedAtIsNull(customer);

        assertEquals(expected, result);
        verify(serviceOrderRepository).findByCustomerAndFinishedAtIsNull(customer);
    }

    @Test
    void findByVehicleAndFinishedAtIsNull_shouldReturnOpenOrders() {
        Vehicle vehicle = mock(Vehicle.class);
        List<ServiceOrder> expected = List.of(mock(ServiceOrder.class));

        when(serviceOrderRepository.findByVehicleAndFinishedAtIsNull(vehicle)).thenReturn(expected);

        List<ServiceOrder> result = gateway.findByVehicleAndFinishedAtIsNull(vehicle);

        assertEquals(expected, result);
        verify(serviceOrderRepository).findByVehicleAndFinishedAtIsNull(vehicle);
    }

    @Test
    void save_shouldReturnSavedOrder() {
        ServiceOrder input = mock(ServiceOrder.class);
        ServiceOrder saved = mock(ServiceOrder.class);

        when(serviceOrderRepository.save(input)).thenReturn(saved);

        ServiceOrder result = gateway.save(input);

        assertEquals(saved, result);
        verify(serviceOrderRepository).save(input);
    }

}
