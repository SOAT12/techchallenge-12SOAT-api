package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.NotificationGateway;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.NotificationRepository;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.NotificationPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.NotificationUseCase;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;

import java.util.List;

public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationPresenter notificationPresenter;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
        this.notificationPresenter = new NotificationPresenter();
    }

    public List<NotificationResponseDTO> getAllNotifications() {
        NotificationGateway notificationGateway = new NotificationGateway(notificationRepository);
        NotificationUseCase notificationUseCase = new NotificationUseCase(notificationGateway);
        return notificationUseCase.getAllNotifications().stream()
                .map(notificationPresenter::toNotificationResponseDTO)
                .toList();
    }

    public List<NotificationResponseDTO> getNotificationsByEmployeeId(Long employeeId) {
        NotificationGateway notificationGateway = new NotificationGateway(notificationRepository);
        NotificationUseCase notificationUseCase = new NotificationUseCase(notificationGateway);
        return notificationUseCase.getNotificationsByEmployeeId(employeeId).stream()
                .map(notificationPresenter::toNotificationResponseDTO)
                .toList();
    }

    public NotificationResponseDTO createNotification(NotificationRequestDTO notificationRequestDTO) {
        NotificationGateway notificationGateway = new NotificationGateway(notificationRepository);
        NotificationUseCase notificationUseCase = new NotificationUseCase(notificationGateway);
        Notification notification = notificationUseCase.createNotification(notificationRequestDTO);
        return notificationPresenter.toNotificationResponseDTO(notification);
    }

    public void deleteNotification(Long id) {
        NotificationGateway notificationGateway = new NotificationGateway(notificationRepository);
        NotificationUseCase notificationUseCase = new NotificationUseCase(notificationGateway);
        notificationUseCase.deleteNotification(id);
    }
}
