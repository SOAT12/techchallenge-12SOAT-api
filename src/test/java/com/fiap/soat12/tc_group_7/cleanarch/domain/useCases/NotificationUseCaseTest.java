package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.NotificationGateway;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationUseCaseTest {

    @Mock
    private NotificationGateway notificationGateway;

    @Mock
    private EmployeeUseCase employeeUseCase;

    @InjectMocks
    private NotificationUseCase notificationUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class GetAllNotifications {
        @Test
        void shouldReturnAllNotifications() {
            // Arrange
            when(notificationGateway.findAll()).thenReturn(List.of(Notification.builder().build()));

            // Act
            List<Notification> result = notificationUseCase.getAllNotifications();

            // Assert
            assertFalse(result.isEmpty());
            verify(notificationGateway).findAll();
        }
    }

    @Nested
    class GetNotificationsByEmployeeId {
        @Test
        void shouldReturnNotificationsForEmployee() {
            // Arrange
            Long employeeId = 1L;
            when(notificationGateway.findByEmployees_Id(employeeId)).thenReturn(List.of(Notification.builder().build()));

            // Act
            List<Notification> result = notificationUseCase.getNotificationsByEmployeeId(employeeId);

            // Assert
            assertFalse(result.isEmpty());
            verify(notificationGateway).findByEmployees_Id(employeeId);
        }
    }

    @Nested
    class CreateNotification {
        @Test
        void shouldCreateNotification() {
            // Arrange
            NotificationRequestDTO dto = new NotificationRequestDTO();
            dto.setMessage("Test message");
            Notification notification = Notification.builder().message("Test message").build();
            when(notificationGateway.save(any(Notification.class))).thenReturn(notification);

            // Act
            Notification result = notificationUseCase.createNotification(dto);

            // Assert
            assertNotNull(result);
            assertEquals("Test message", result.getMessage());
            verify(notificationGateway).save(any(Notification.class));
        }
    }

    @Nested
    class DeleteNotification {
        @Test
        void shouldDeleteNotification() {
            // Arrange
            Long id = 1L;
            Notification notification = Notification.builder().id(id).build();
            when(notificationGateway.findById(id)).thenReturn(Optional.of(notification));

            // Act
            notificationUseCase.deleteNotification(id);

            // Assert
            verify(notificationGateway).delete(notification);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenDeleting() {
            // Arrange
            Long id = 1L;
            when(notificationGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> notificationUseCase.deleteNotification(id));
            verify(notificationGateway, never()).delete(any());
        }
    }

    @Nested
    class NotifyMechanicAssignedToOS {
        @Test
        void shouldNotifyMechanic() {
            // Arrange
            ServiceOrder so = ServiceOrder.builder().id(1L).build();
            Employee employee = Employee.builder().id(1L).build();

            // Act
            notificationUseCase.notifyMechanicAssignedToOS(so, employee);

            // Assert
            verify(notificationGateway).save(any(Notification.class));
        }
    }

    @Nested
    class NotifyMechanicOSApproved {
        @Test
        void shouldNotifyMechanic() {
            // Arrange
            ServiceOrder so = ServiceOrder.builder().id(1L).build();
            Employee employee = Employee.builder().id(1L).build();

            // Act
            notificationUseCase.notifyMechanicOSApproved(so, employee);

            // Assert
            verify(notificationGateway).save(any(Notification.class));
        }
    }

    @Nested
    class NotifyManagersOutOfStock {
        @Test
        void shouldNotifyManagers() {
            // Arrange
            ServiceOrder so = ServiceOrder.builder().id(1L).build();
            when(employeeUseCase.getByEmployeeFunction(anyString())).thenReturn(List.of(Employee.builder().build()));

            // Act
            notificationUseCase.notifyManagersOutOfStock(so);

            // Assert
            verify(notificationGateway).save(any(Notification.class));
        }

        @Test
        void shouldNotNotifyIfNoManagers() {
            // Arrange
            ServiceOrder so = ServiceOrder.builder().id(1L).build();
            when(employeeUseCase.getByEmployeeFunction(anyString())).thenReturn(Collections.emptyList());

            // Act
            notificationUseCase.notifyManagersOutOfStock(so);

            // Assert
            verify(notificationGateway, never()).save(any(Notification.class));
        }
    }

    @Nested
    class NotifyAttendantsOSCompleted {
        @Test
        void shouldNotifyAttendants() {
            // Arrange
            ServiceOrder so = ServiceOrder.builder().id(1L).build();
            when(employeeUseCase.getByEmployeeFunction(anyString())).thenReturn(List.of(Employee.builder().build()));

            // Act
            notificationUseCase.notifyAttendantsOSCompleted(so);

            // Assert
            verify(notificationGateway).save(any(Notification.class));
        }

        @Test
        void shouldNotNotifyIfNoAttendants() {
            // Arrange
            ServiceOrder so = ServiceOrder.builder().id(1L).build();
            when(employeeUseCase.getByEmployeeFunction(anyString())).thenReturn(Collections.emptyList());

            // Act
            notificationUseCase.notifyAttendantsOSCompleted(so);

            // Assert
            verify(notificationGateway, never()).save(any(Notification.class));
        }
    }
}
