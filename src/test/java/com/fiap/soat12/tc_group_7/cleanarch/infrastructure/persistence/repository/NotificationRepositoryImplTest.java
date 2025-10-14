package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.NotificationJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.NotificationMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.NotificationJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class NotificationRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationJpaRepository notificationJpaRepository;

    @InjectMocks
    private NotificationRepositoryImpl notificationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_shouldReturnMappedNotifications() {
        NotificationJpaEntity entity1 = mock(NotificationJpaEntity.class);
        NotificationJpaEntity entity2 = mock(NotificationJpaEntity.class);

        Notification notification1 = mock(Notification.class);
        Notification notification2 = mock(Notification.class);

        when(notificationJpaRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(notificationMapper.toNotification(entity1)).thenReturn(notification1);
        when(notificationMapper.toNotification(entity2)).thenReturn(notification2);

        List<Notification> notifications = notificationRepository.findAll();

        assertEquals(2, notifications.size());
        assertTrue(notifications.contains(notification1));
        assertTrue(notifications.contains(notification2));

        verify(notificationJpaRepository).findAll();
        verify(notificationMapper).toNotification(entity1);
        verify(notificationMapper).toNotification(entity2);
    }

    @Test
    void findById_shouldReturnMappedNotification_whenFound() {
        Long id = 1L;
        NotificationJpaEntity entity = mock(NotificationJpaEntity.class);
        Notification notification = mock(Notification.class);

        when(notificationJpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(notificationMapper.toNotification(entity)).thenReturn(notification);

        Optional<Notification> result = notificationRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(notification, result.get());

        verify(notificationJpaRepository).findById(id);
        verify(notificationMapper).toNotification(entity);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        Long id = 1L;

        when(notificationJpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Notification> result = notificationRepository.findById(id);

        assertTrue(result.isEmpty());

        verify(notificationJpaRepository).findById(id);
        verifyNoInteractions(notificationMapper);
    }

    @Test
    void findByEmployeesId_shouldReturnMappedNotifications() {
        Long employeeId = 2L;

        NotificationJpaEntity entity1 = mock(NotificationJpaEntity.class);
        NotificationJpaEntity entity2 = mock(NotificationJpaEntity.class);

        Notification notification1 = mock(Notification.class);
        Notification notification2 = mock(Notification.class);

        when(notificationJpaRepository.findByEmployees_Id(employeeId)).thenReturn(List.of(entity1, entity2));
        when(notificationMapper.toNotification(entity1)).thenReturn(notification1);
        when(notificationMapper.toNotification(entity2)).thenReturn(notification2);

        List<Notification> notifications = notificationRepository.findByEmployees_Id(employeeId);

        assertEquals(2, notifications.size());
        assertTrue(notifications.contains(notification1));
        assertTrue(notifications.contains(notification2));

        verify(notificationJpaRepository).findByEmployees_Id(employeeId);
        verify(notificationMapper).toNotification(entity1);
        verify(notificationMapper).toNotification(entity2);
    }

    @Test
    void save_shouldSaveAndReturnNotification() {
        Notification notification = mock(Notification.class);
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        when(notification.getServiceOrder()).thenReturn(serviceOrder);
        when(serviceOrder.getId()).thenReturn(10L);

        Employee employee1 = mock(Employee.class);
        Employee employee2 = mock(Employee.class);
        when(employee1.getId()).thenReturn(1L);
        when(employee2.getId()).thenReturn(2L);
        when(notification.getEmployees()).thenReturn(Set.of(employee1, employee2));

        ServiceOrderEntity serviceOrderEntity = mock(ServiceOrderEntity.class);
        EmployeeJpaEntity employeeEntity1 = mock(EmployeeJpaEntity.class);
        EmployeeJpaEntity employeeEntity2 = mock(EmployeeJpaEntity.class);

        NotificationJpaEntity notificationJpaEntity = mock(NotificationJpaEntity.class);
        NotificationJpaEntity savedJpaEntity = mock(NotificationJpaEntity.class);
        Notification savedNotification = mock(Notification.class);

        when(entityManager.getReference(ServiceOrderEntity.class, 10L)).thenReturn(serviceOrderEntity);
        when(entityManager.getReference(EmployeeJpaEntity.class, 1L)).thenReturn(employeeEntity1);
        when(entityManager.getReference(EmployeeJpaEntity.class, 2L)).thenReturn(employeeEntity2);

        when(notificationMapper.toNotificationJpaEntity(eq(notification), eq(serviceOrderEntity), anySet())).thenReturn(notificationJpaEntity);
        when(notificationJpaRepository.save(notificationJpaEntity)).thenReturn(savedJpaEntity);
        when(notificationMapper.toNotification(savedJpaEntity)).thenReturn(savedNotification);

        Notification result = notificationRepository.save(notification);

        assertEquals(savedNotification, result);

        verify(entityManager).getReference(ServiceOrderEntity.class, 10L);
        verify(entityManager).getReference(EmployeeJpaEntity.class, 1L);
        verify(entityManager).getReference(EmployeeJpaEntity.class, 2L);
        verify(notificationJpaRepository).save(notificationJpaEntity);
        verify(notificationMapper).toNotification(savedJpaEntity);
    }

    @Test
    void delete_shouldDeleteNotification() {
        Notification notification = mock(Notification.class);
        ServiceOrder serviceOrder = mock(ServiceOrder.class);
        when(notification.getServiceOrder()).thenReturn(serviceOrder);
        when(serviceOrder.getId()).thenReturn(10L);

        Employee employee1 = mock(Employee.class);
        Employee employee2 = mock(Employee.class);
        when(employee1.getId()).thenReturn(1L);
        when(employee2.getId()).thenReturn(2L);
        when(notification.getEmployees()).thenReturn(Set.of(employee1, employee2));

        ServiceOrderEntity serviceOrderEntity = mock(ServiceOrderEntity.class);
        EmployeeJpaEntity employeeEntity1 = mock(EmployeeJpaEntity.class);
        EmployeeJpaEntity employeeEntity2 = mock(EmployeeJpaEntity.class);

        NotificationJpaEntity notificationJpaEntity = mock(NotificationJpaEntity.class);

        when(entityManager.getReference(ServiceOrderEntity.class, 10L)).thenReturn(serviceOrderEntity);
        when(entityManager.getReference(EmployeeJpaEntity.class, 1L)).thenReturn(employeeEntity1);
        when(entityManager.getReference(EmployeeJpaEntity.class, 2L)).thenReturn(employeeEntity2);

        when(notificationMapper.toNotificationJpaEntity(eq(notification), eq(serviceOrderEntity), anySet())).thenReturn(notificationJpaEntity);

        notificationRepository.delete(notification);

        verify(entityManager).getReference(ServiceOrderEntity.class, 10L);
        verify(entityManager).getReference(EmployeeJpaEntity.class, 1L);
        verify(entityManager).getReference(EmployeeJpaEntity.class, 2L);
        verify(notificationJpaRepository).delete(notificationJpaEntity);
    }

}
