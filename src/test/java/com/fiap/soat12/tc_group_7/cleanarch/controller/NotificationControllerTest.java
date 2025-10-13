package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.NotificationUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.NotificationController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.NotificationPresenter;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class NotificationControllerTest {

    private NotificationUseCase notificationUseCase;
    private NotificationPresenter notificationPresenter;
    private NotificationController controller;

    @BeforeEach
    void setup() {
        notificationUseCase = mock(NotificationUseCase.class);
        notificationPresenter = mock(NotificationPresenter.class);
        controller = new NotificationController(notificationUseCase, notificationPresenter);
    }

    @Test
    void getAllNotifications_shouldReturnMappedDTOs() {
        Notification notification = mock(Notification.class);
        NotificationResponseDTO dto = new NotificationResponseDTO();
        List<Notification> notifications = List.of(notification);

        when(notificationUseCase.getAllNotifications()).thenReturn(notifications);
        when(notificationPresenter.toNotificationResponseDTO(notification)).thenReturn(dto);

        List<NotificationResponseDTO> result = controller.getAllNotifications();

        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());

        verify(notificationUseCase).getAllNotifications();
        verify(notificationPresenter).toNotificationResponseDTO(notification);
    }

    @Test
    void getNotificationsByEmployeeId_shouldReturnMappedDTOs() {
        Long employeeId = 123L;

        Notification notification = mock(Notification.class);
        NotificationResponseDTO dto = new NotificationResponseDTO();
        List<Notification> notifications = List.of(notification);

        when(notificationUseCase.getNotificationsByEmployeeId(employeeId)).thenReturn(notifications);
        when(notificationPresenter.toNotificationResponseDTO(notification)).thenReturn(dto);

        List<NotificationResponseDTO> result = controller.getNotificationsByEmployeeId(employeeId);

        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());

        verify(notificationUseCase).getNotificationsByEmployeeId(employeeId);
        verify(notificationPresenter).toNotificationResponseDTO(notification);
    }

    @Test
    void createNotification_shouldReturnMappedDTO() {
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        Notification notification = mock(Notification.class);
        NotificationResponseDTO dto = new NotificationResponseDTO();

        when(notificationUseCase.createNotification(requestDTO)).thenReturn(notification);
        when(notificationPresenter.toNotificationResponseDTO(notification)).thenReturn(dto);

        NotificationResponseDTO result = controller.createNotification(requestDTO);

        assertSame(dto, result);

        verify(notificationUseCase).createNotification(requestDTO);
        verify(notificationPresenter).toNotificationResponseDTO(notification);
    }

    @Test
    void deleteNotification_shouldCallUseCase() {
        Long id = 42L;

        // Método deleteNotification não retorna nada, só verifica chamada
        controller.deleteNotification(id);

        verify(notificationUseCase).deleteNotification(id);
        verifyNoMoreInteractions(notificationPresenter);
    }

}
