package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.NotificationUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.NotificationPresenter;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationControllerTest {

    @Mock
    private NotificationUseCase notificationUseCase;

    @Mock
    private NotificationPresenter notificationPresenter;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class GetAllNotifications {
        @Test
        void shouldGetAllNotifications() {
            // Arrange
            when(notificationUseCase.getAllNotifications()).thenReturn(List.of(Notification.builder().build()));
            when(notificationPresenter.toNotificationResponseDTO(any())).thenReturn(new NotificationResponseDTO());

            // Act
            List<NotificationResponseDTO> result = notificationController.getAllNotifications();

            // Assert
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class GetNotificationsByEmployeeId {
        @Test
        void shouldGetNotificationsByEmployeeId() {
            // Arrange
            Long employeeId = 1L;
            when(notificationUseCase.getNotificationsByEmployeeId(employeeId)).thenReturn(List.of(Notification.builder().build()));
            when(notificationPresenter.toNotificationResponseDTO(any())).thenReturn(new NotificationResponseDTO());

            // Act
            List<NotificationResponseDTO> result = notificationController.getNotificationsByEmployeeId(employeeId);

            // Assert
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class CreateNotification {
        @Test
        void shouldCreateNotification() {
            // Arrange
            NotificationRequestDTO dto = new NotificationRequestDTO();
            when(notificationUseCase.createNotification(dto)).thenReturn(Notification.builder().build());
            when(notificationPresenter.toNotificationResponseDTO(any())).thenReturn(new NotificationResponseDTO());

            // Act
            NotificationResponseDTO result = notificationController.createNotification(dto);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class DeleteNotification {
        @Test
        void shouldDeleteNotification() {
            // Arrange
            Long id = 1L;

            // Act
            notificationController.deleteNotification(id);

            // Assert
            verify(notificationUseCase).deleteNotification(id);
        }
    }
}