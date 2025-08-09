package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.util.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.fiap.soat12.tc_group_7.util.Status.getStatusesForPendingOrders;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ServiceOrderServiceTest {

    @InjectMocks
    private ServiceOrderService serviceOrderService;
    @Mock
    private ServiceOrderRepository serviceOrderRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void testFindMostAvailableEmployee_withAvailableEmployees() {
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

        when(employeeRepository.findAllByActiveTrue())
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
    void testFindMostAvailableEmployee_withNoActiveEmployees() {
        // Simulando que não existem funcionários ativos
        when(employeeRepository.findAllByActiveTrue()).thenReturn(Collections.emptyList());

        // Executar o método e verificar a exceção
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            serviceOrderService.findMostAvailableEmployee();
        });
        assertEquals("Nenhum mecânico disponível", exception.getMessage());
    }

    @Test
    void testFindMostAvailableEmployee_withEqualActiveOrders() {
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

        when(employeeRepository.findAllByActiveTrue())
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
}
