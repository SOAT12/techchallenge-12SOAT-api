package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.NotificationJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NotificationMapper {

    private final ServiceOrderMapper serviceOrderMapper;
    private final EmployeeMapper employeeMapper;

    public Notification toNotification(NotificationJpaEntity notificationJpaEntity) {
        var serviceOrder = serviceOrderMapper.toServiceOrder(notificationJpaEntity.getServiceOrder());
        var employees = notificationJpaEntity.getEmployees().stream()
                .map(employeeMapper::toEmployee)
                .collect(Collectors.toSet());

        return Notification.builder()
                .id(notificationJpaEntity.getId())
                .message(notificationJpaEntity.getMessage())
                .isRead(notificationJpaEntity.isRead())
                .serviceOrder(serviceOrder)
                .employees(employees)
                .createdAt(notificationJpaEntity.getCreatedAt())
                .updatedAt(notificationJpaEntity.getUpdatedAt())
                .build();
    }

    public NotificationJpaEntity toNotificationJpaEntity(Notification notification,
                                                         ServiceOrderEntity serviceOrderEntity,
                                                         Set<EmployeeJpaEntity> employeeJpaEntities) {
        return NotificationJpaEntity.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .serviceOrder(serviceOrderEntity)
                .employees(employeeJpaEntities)
                .build();
    }

}
