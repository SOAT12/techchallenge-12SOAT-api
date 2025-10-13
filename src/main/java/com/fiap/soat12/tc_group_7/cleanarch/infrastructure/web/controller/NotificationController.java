package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.NotificationUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.NotificationPresenter;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;

import java.util.List;

public class NotificationController {

    private final NotificationUseCase notificationUseCase;
    private final NotificationPresenter notificationPresenter;

    public NotificationController(NotificationUseCase notificationUseCase, NotificationPresenter notificationPresenter) {
        this.notificationUseCase = notificationUseCase;
        this.notificationPresenter = notificationPresenter;
    }

    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationUseCase.getAllNotifications().stream()
                .map(notificationPresenter::toNotificationResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> getNotificationsByEmployeeId(Long employeeId) {
        return notificationUseCase.getNotificationsByEmployeeId(employeeId).stream()
                .map(notificationPresenter::toNotificationResponseDTO)
                .toList();
    }

    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        Notification notification = notificationUseCase.createNotification(notificationRequestDTO);
        return notificationPresenter.toNotificationResponseDTO(notification);
    }

    public void deleteNotification(Long id) {
        notificationUseCase.deleteNotification(id);
    }
}
