package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationGatewayTest {

    @Mock
    private NotificationRepository notificationRepository;

    private NotificationGateway notificationGateway;

    @BeforeEach
    void setUp() {
        notificationGateway = new NotificationGateway(notificationRepository);
    }

    @Test
    void findAll_shouldReturnListOfNotifications() {
        List<Notification> notifications = List.of(new Notification(), new Notification());
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationGateway.findAll();

        assertEquals(2, result.size());
        verify(notificationRepository).findAll();
    }

    @Test
    void findById_shouldReturnNotificationOptional() {
        Long id = 1L;
        Notification notification = new Notification();
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));

        Optional<Notification> result = notificationGateway.findById(id);

        assertTrue(result.isPresent());
        verify(notificationRepository).findById(id);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        Long id = 1L;
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Notification> result = notificationGateway.findById(id);

        assertFalse(result.isPresent());
        verify(notificationRepository).findById(id);
    }

    @Test
    void findByEmployeesId_shouldReturnListOfNotifications() {
        Long employeeId = 10L;
        List<Notification> notifications = List.of(new Notification());
        when(notificationRepository.findByEmployees_Id(employeeId)).thenReturn(notifications);

        List<Notification> result = notificationGateway.findByEmployees_Id(employeeId);

        assertEquals(1, result.size());
        verify(notificationRepository).findByEmployees_Id(employeeId);
    }

    @Test
    void save_shouldReturnSavedNotification() {
        Notification notification = new Notification();
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationGateway.save(notification);

        assertNotNull(result);
        verify(notificationRepository).save(notification);
    }

    @Test
    void delete_shouldCallRepositoryDelete() {
        Notification notification = new Notification();

        notificationGateway.delete(notification);

        verify(notificationRepository).delete(notification);
    }

}
