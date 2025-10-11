package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.NotificationRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.NotificationJpaEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class NotificationGateway {

    private final NotificationRepository notificationRepository;

    public List<Notification> findAll() {
        return notificationRepository.findAll().stream()
                .map(this::toNotification)
                .toList();
    }

    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id)
                .map(this::toNotification);
    }

    public List<Notification> findByEmployees_Id(Long employeeId) {
        return notificationRepository.findByEmployees_Id(employeeId).stream()
                .map(this::toNotification)
                .toList();
    }

    public Notification save(Notification notification) {
        var notificationJpaEntity = notificationRepository.save(this.toNotificationJpaEntity(notification));
        return this.toNotification(notificationJpaEntity);
    }

    public void delete(Notification notification) {
        notificationRepository.delete(this.toNotificationJpaEntity(notification));
    }

    private Notification toNotification(NotificationJpaEntity notificationJpaEntity) {
        return Notification.builder()
                .id(notificationJpaEntity.getId())
                .message(notificationJpaEntity.getMessage())
                .isRead(notificationJpaEntity.isRead())
                .serviceOrder(notificationJpaEntity.getServiceOrder())
                .employees(notificationJpaEntity.getEmployees())
                .createdAt(notificationJpaEntity.getCreatedAt())
                .updatedAt(notificationJpaEntity.getUpdatedAt())
                .build();
    }

    private NotificationJpaEntity toNotificationJpaEntity(Notification notification) {
        return NotificationJpaEntity.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .serviceOrder(notification.getServiceOrder())
                .employees(notification.getEmployees())
                .build();
    }
}
