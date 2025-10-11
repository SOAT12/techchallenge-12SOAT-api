package com.fiap.soat12.tc_group_7.cleanarch.usecase;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.NotificationGateway;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NotificationUseCase {

    public static final String NOTIFICATION_NOT_FOUND_MESSAGE = "Notificação não encontrada.";

    private final NotificationGateway notificationGateway;

    public List<Notification> getAllNotifications() {
        return notificationGateway.findAll();
    }

    public List<Notification> getNotificationsByEmployeeId(Long employeeId) {
        return notificationGateway.findByEmployees_Id(employeeId);
    }

    public Notification createNotification(NotificationRequestDTO notificationRequestDTO) {
        // TODO vinicius.filho | Consumir use cases para buscar service order e employees
        var notification = Notification.builder()
                .message(notificationRequestDTO.getMessage())
                //.serviceOrder(notificationRequestDTO.getServiceOrder())
                //.employees(notificationRequestDTO.getEmployees())
                .build();
        return notificationGateway.save(notification);
    }

    public void deleteNotification(Long id) {
        var notification = notificationGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(NOTIFICATION_NOT_FOUND_MESSAGE));
        notificationGateway.delete(notification);
    }

}
