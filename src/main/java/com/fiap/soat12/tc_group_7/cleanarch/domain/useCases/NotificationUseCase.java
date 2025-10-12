package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.NotificationGateway;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class NotificationUseCase {

    protected static final String MESSAGE_ASSIGNED_TO_OS = "Você foi atribuído à OS %d.";
    protected static final String NOTIFICATION_NOT_FOUND_MESSAGE = "Notificação não encontrada.";

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

    public void notifyMechanicAssignedToOS(ServiceOrder serviceOrder, Employee employee) {
        Notification notification = new Notification();
        notification.setServiceOrder(serviceOrder);
        notification.setEmployees(Set.of(employee));
        notification.setMessage(String.format(MESSAGE_ASSIGNED_TO_OS, serviceOrder.getId()));
        notificationGateway.save(notification);
    }

}
