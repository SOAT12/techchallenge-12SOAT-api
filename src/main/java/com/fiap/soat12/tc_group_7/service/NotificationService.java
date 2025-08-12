package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.Notification;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.mapper.NotificationMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.repository.NotificationRepository;
import com.fiap.soat12.tc_group_7.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    protected static final String MESSAGE_ASSIGNED_TO_OS = "Você foi atribuído à OS %d.";
    protected static final String MESSAGE_OS_APPROVED = "A OS %d foi aprovada.";
    protected static final String MESSAGE_OUT_OF_STOCK = "As peças da OS %d não estão disponíveis no estoque.";
    protected static final String MESSAGE_OS_COMPLETED = "A OS %d foi finalizada.";
    protected static final String ATTENDANT_DESCRIPTION = "Atendente";
    protected static final String MANAGER_DESCRIPTION = "Gestor";

    private final NotificationRepository notificationRepository;
    private final ServiceOrderRepository serviceOrderRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationMapper notificationMapper;

    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toNotificationResponseDTO)
                .collect(Collectors.toList());
    }

    public List<NotificationResponseDTO> getNotificationsByEmployeeId(Long employeeId) {
        return notificationRepository.findByEmployees_Id(employeeId).stream()
                .map(notificationMapper::toNotificationResponseDTO)
                .collect(Collectors.toList());
    }

    public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        var serviceOrder = serviceOrderRepository.findById(requestDTO.getServiceOrderId())
                .orElseThrow(() -> new NotFoundException("Ordem de serviço não encontrada"));

        Set<Long> employeeIds = requestDTO.getEmployeeIds();
        Set<Employee> employeeList = employeeRepository.findAllById(employeeIds)
                .stream().collect(Collectors.toSet());
        if (employeeList.size() != employeeIds.size()) {
            throw new NotFoundException("Um ou mais funcionários não foram encontrados");
        }

        Notification savedNotification = notificationRepository.save(notificationMapper.toNotification(requestDTO, serviceOrder, employeeList));
        return notificationMapper.toNotificationResponseDTO(savedNotification);
    }

    public void deleteNotification(Long id) {
        var notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Notificação não encontrada"));
        notificationRepository.delete(notification);
    }

    public void notifyMechanicAssignedToOS(ServiceOrder serviceOrder, Employee employee) {
        Notification notification = new Notification();
        notification.setServiceOrder(serviceOrder);
        notification.setEmployees(Set.of(employee));
        notification.setMessage(String.format(MESSAGE_ASSIGNED_TO_OS, serviceOrder.getId()));
        notificationRepository.save(notification);
    }

    public void notifyMechanicOSApproved(ServiceOrder serviceOrder, Employee employee) {
        Notification notification = new Notification();
        notification.setServiceOrder(serviceOrder);
        notification.setEmployees(Set.of(employee));
        notification.setMessage(String.format(MESSAGE_OS_APPROVED, serviceOrder.getId()));
        notificationRepository.save(notification);
    }

    // TODO - Adicionar chamada no fluxo de notificar gestor
    public void notifyManagersOutOfStock(ServiceOrder serviceOrder) {
        Set<Employee> activeEmployees = employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MANAGER_DESCRIPTION)
                .stream().collect(Collectors.toSet());

        if(!activeEmployees.isEmpty()) {
            Notification notification = new Notification();
            notification.setServiceOrder(serviceOrder);
            notification.setEmployees(activeEmployees);
            notification.setMessage(String.format(MESSAGE_OUT_OF_STOCK, serviceOrder.getId()));
            notificationRepository.save(notification);
        }
    }

    public void notifyAttendantsOSCompleted(ServiceOrder serviceOrder) {
        Set<Employee> activeEmployees = employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(ATTENDANT_DESCRIPTION)
                .stream().collect(Collectors.toSet());

        if(!activeEmployees.isEmpty()) {
            Notification notification = new Notification();
            notification.setServiceOrder(serviceOrder);
            notification.setEmployees(activeEmployees);
            notification.setMessage(String.format(MESSAGE_OS_COMPLETED, serviceOrder.getId()));
            notificationRepository.save(notification);
        }
    }

}
