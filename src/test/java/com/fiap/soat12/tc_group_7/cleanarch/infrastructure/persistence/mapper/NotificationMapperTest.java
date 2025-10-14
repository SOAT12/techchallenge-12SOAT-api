package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.NotificationJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class NotificationMapperTest {

    @Mock
    private ServiceOrderMapper serviceOrderMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private NotificationMapper notificationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toNotification_shouldMapAllFieldsCorrectly() {
        // Setup NotificationJpaEntity mock and its dependencies
        NotificationJpaEntity notificationJpaEntity = mock(NotificationJpaEntity.class);
        ServiceOrderEntity serviceOrderEntity = mock(ServiceOrderEntity.class);
        EmployeeJpaEntity employeeJpaEntity1 = mock(EmployeeJpaEntity.class);
        EmployeeJpaEntity employeeJpaEntity2 = mock(EmployeeJpaEntity.class);

        when(notificationJpaEntity.getId()).thenReturn(1L);
        when(notificationJpaEntity.getMessage()).thenReturn("Test message");
        when(notificationJpaEntity.isRead()).thenReturn(true);
        when(notificationJpaEntity.getServiceOrder()).thenReturn(serviceOrderEntity);
        when(notificationJpaEntity.getEmployees()).thenReturn(Set.of(employeeJpaEntity1, employeeJpaEntity2));
        when(notificationJpaEntity.getCreatedAt()).thenReturn(new Date(1000L));
        when(notificationJpaEntity.getUpdatedAt()).thenReturn(new Date(2000L));

        // Mock serviceOrderMapper
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        when(serviceOrderMapper.toServiceOrder(serviceOrderEntity)).thenReturn(serviceOrder);

        // Mock employeeMapper
        Employee employee1 = mock(Employee.class);
        Employee employee2 = mock(Employee.class);
        when(employeeMapper.toEmployee(employeeJpaEntity1)).thenReturn(employee1);
        when(employeeMapper.toEmployee(employeeJpaEntity2)).thenReturn(employee2);

        // Call method under test
        Notification notification = notificationMapper.toNotification(notificationJpaEntity);

        // Assertions
        assertEquals(1L, notification.getId());
        assertEquals("Test message", notification.getMessage());
        assertTrue(notification.isRead());
        assertEquals(serviceOrder, notification.getServiceOrder());
        assertEquals(2, notification.getEmployees().size());
        assertTrue(notification.getEmployees().contains(employee1));
        assertTrue(notification.getEmployees().contains(employee2));
        assertEquals(new Date(1000L), notification.getCreatedAt());
        assertEquals(new Date(2000L), notification.getUpdatedAt());

        verify(serviceOrderMapper).toServiceOrder(serviceOrderEntity);
        verify(employeeMapper).toEmployee(employeeJpaEntity1);
        verify(employeeMapper).toEmployee(employeeJpaEntity2);
    }

    @Test
    void toNotificationJpaEntity_shouldMapAllFieldsCorrectly() {
        // Setup Notification mock and dependencies
        Notification notification = mock(Notification.class);
        ServiceOrderEntity serviceOrderEntity = mock(ServiceOrderEntity.class);
        EmployeeJpaEntity employeeJpaEntity1 = mock(EmployeeJpaEntity.class);
        EmployeeJpaEntity employeeJpaEntity2 = mock(EmployeeJpaEntity.class);

        when(notification.getId()).thenReturn(1L);
        when(notification.getMessage()).thenReturn("Test message");
        when(notification.isRead()).thenReturn(true);
        when(notification.getServiceOrder()).thenReturn(mock(ServiceOrder.class));
        when(notification.getEmployees()).thenReturn(Set.of(mock(Employee.class), mock(Employee.class)));

        Set<EmployeeJpaEntity> employeeJpaEntities = Set.of(employeeJpaEntity1, employeeJpaEntity2);

        // Call method under test
        NotificationJpaEntity result = notificationMapper.toNotificationJpaEntity(notification, serviceOrderEntity, employeeJpaEntities);

        // Assertions
        assertEquals(1L, result.getId());
        assertEquals("Test message", result.getMessage());
        assertTrue(result.isRead());
        assertEquals(serviceOrderEntity, result.getServiceOrder());
        assertEquals(employeeJpaEntities, result.getEmployees());
    }

}
