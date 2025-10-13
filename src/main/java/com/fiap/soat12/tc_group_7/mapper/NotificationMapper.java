package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class NotificationMapper {

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

    public Notification toNotification(NotificationRequestDTO requestDTO, ServiceOrderEntity serviceOrder, Set<Employee> employees) {
        return Notification.builder()
                .message(requestDTO.getMessage())
                .isRead(false)
                .serviceOrder(serviceOrder)
                .employees(employees)
                .build();
    }

}
