package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;

import java.util.List;

public class NotificationPresenter {

    public NotificationResponseDTO toNotificationResponseDTO(Notification notification) {
        NotificationResponseDTO.ServiceOrderDTO serviceOrderDTO = NotificationResponseDTO.ServiceOrderDTO.builder()
                .id(notification.getServiceOrder().getId())
                .status(notification.getServiceOrder().getStatus())
                .totalValue(notification.getServiceOrder().getTotalValue())
                .createdAt(notification.getServiceOrder().getCreatedAt())
                .finishedAt(notification.getServiceOrder().getFinishedAt())
                .build();

        List<NotificationResponseDTO.EmployeeDTO> employeeDTOs = notification.getEmployees().stream()
                .map(employee -> NotificationResponseDTO.EmployeeDTO.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .build())
                .toList();

        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .serviceOrder(serviceOrderDTO)
                .employees(employeeDTOs)
                .build();
    }

}
