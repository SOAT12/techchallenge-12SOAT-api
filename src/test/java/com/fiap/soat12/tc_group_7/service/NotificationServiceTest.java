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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.fiap.soat12.tc_group_7.service.NotificationService.ATTENDANT_DESCRIPTION;
import static com.fiap.soat12.tc_group_7.service.NotificationService.MANAGER_DESCRIPTION;
import static com.fiap.soat12.tc_group_7.service.NotificationService.MESSAGE_ASSIGNED_TO_OS;
import static com.fiap.soat12.tc_group_7.service.NotificationService.MESSAGE_OS_APPROVED;
import static com.fiap.soat12.tc_group_7.service.NotificationService.MESSAGE_OS_COMPLETED;
import static com.fiap.soat12.tc_group_7.service.NotificationService.MESSAGE_OUT_OF_STOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;
    @Mock
    private NotificationRepository notificationRepository;
    @Mock
    private ServiceOrderRepository serviceOrderRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private NotificationMapper notificationMapper;

    @Test
    void getAllNotifications_withSuccess() {
        // Arrange
        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setMessage("Notificação 1");

        Notification notification2 = new Notification();
        notification2.setId(2L);
        notification2.setMessage("Notificação 2");

        List<Notification> notifications = Arrays.asList(notification1, notification2);
        NotificationResponseDTO dto1 = NotificationResponseDTO.builder()
                .id(1L)
                .message("Notificação 1")
                .isRead(false)
                .build();
        NotificationResponseDTO dto2 = NotificationResponseDTO.builder()
                .id(2L)
                .message("Notificação 2")
                .isRead(false)
                .build();

        when(notificationRepository.findAll()).thenReturn(notifications);
        when(notificationMapper.toNotificationResponseDTO(notification1)).thenReturn(dto1);
        when(notificationMapper.toNotificationResponseDTO(notification2)).thenReturn(dto2);

        // Act
        List<NotificationResponseDTO> result = notificationService.getAllNotifications();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void getNotificationsByEmployeeId_withSuccess() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        Notification notification1 = new Notification();
        notification1.setId(1L);
        notification1.setMessage("Notificação do funcionário");

        List<Notification> notifications = Arrays.asList(notification1);
        NotificationResponseDTO dto1 = NotificationResponseDTO.builder()
                .id(1L)
                .message("Notificação do funcionário")
                .isRead(false)
                .build();

        when(notificationRepository.findByEmployees_Id(employeeId)).thenReturn(notifications);
        when(notificationMapper.toNotificationResponseDTO(notification1)).thenReturn(dto1);

        // Act
        List<NotificationResponseDTO> result = notificationService.getNotificationsByEmployeeId(employeeId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMessage()).isEqualTo("Notificação do funcionário");
    }

    @Test
    void createNotification_withSuccess() {
        // Arrange
        NotificationRequestDTO requestDTO = NotificationRequestDTO.builder()
                .message("Nova notificação")
                .serviceOrderId(99L)
                .employeeIds(Set.of(1L, 2L))
                .build();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(99L);

        Employee employee1 = new Employee();
        employee1.setId(1L);
        Employee employee2 = new Employee();
        employee2.setId(2L);

        Set<Employee> employees = Set.of(employee1, employee2);
        Notification notification = new Notification();
        notification.setId(50L);
        notification.setMessage("Nova notificação");

        NotificationResponseDTO responseDTO = NotificationResponseDTO.builder()
                .id(50L)
                .message("Nova notificação")
                .isRead(false)
                .build();

        when(serviceOrderRepository.findById(requestDTO.getServiceOrderId())).thenReturn(Optional.of(serviceOrder));
        when(employeeRepository.findAllById(requestDTO.getEmployeeIds())).thenReturn(new ArrayList<>(employees));
        when(notificationRepository.save(Mockito.any())).thenReturn(notification);
        when(notificationMapper.toNotificationResponseDTO(notification)).thenReturn(responseDTO);
        when(notificationMapper.toNotification(Mockito.any(), Mockito.any(), Mockito.anySet())).thenReturn(notification);

        // Act
        NotificationResponseDTO result = notificationService.createNotification(requestDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(50L);
        assertThat(result.getMessage()).isEqualTo("Nova notificação");
    }

    @Test
    void createNotification_withNotFoundException() {
        // Arrange
        NotificationRequestDTO requestDTO = NotificationRequestDTO.builder()
                .message("Nova notificação")
                .serviceOrderId(99L)
                .employeeIds(Set.of(1L, 2L))
                .build();

        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(99L);

        Employee employee1 = new Employee();
        employee1.setId(1L);

        Set<Employee> employees = Set.of(employee1);

        when(serviceOrderRepository.findById(requestDTO.getServiceOrderId())).thenReturn(Optional.of(serviceOrder));
        when(employeeRepository.findAllById(requestDTO.getEmployeeIds())).thenReturn(new ArrayList<>(employees));

        // Act & Assert
        assertThatThrownBy(() -> notificationService.createNotification(requestDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Um ou mais funcionários não foram encontrados");
    }

    @Test
    void deleteNotification_withSuccess() {
        // Arrange
        Long notificationId = 50L;
        Notification notification = new Notification();
        notification.setId(50L);
        notification.setMessage("Notificação");

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        Mockito.doNothing().when(notificationRepository).delete(notification);

        // Act
        notificationService.deleteNotification(notificationId);

        // Assert
        verify(notificationRepository, times(1)).delete(notification);
    }

    @Test
    void deleteNotification_withNotFoundException() {
        // Arrange
        Long notificationId = 50L;

        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.deleteNotification(notificationId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Notificação não encontrada");
    }

    @Test
    public void notifyMechanicAssignedToOS_withSuccess() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(1L);

        Employee employee = new Employee();
        employee.setId(101L);

        // Act
        notificationService.notifyMechanicAssignedToOS(serviceOrder, employee);

        // Assert
        verify(notificationRepository, times(1)).save(Mockito.argThat(notification ->
                notification.getMessage().equals(String.format(MESSAGE_ASSIGNED_TO_OS, serviceOrder.getId()))
        ));
    }

    @Test
    public void notifyMechanicOSApproved_withSuccess() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(2L);

        Employee employee = new Employee();
        employee.setId(102L);

        // Act
        notificationService.notifyMechanicOSApproved(serviceOrder, employee);

        // Assert
        verify(notificationRepository, times(1)).save(Mockito.argThat(notification ->
                notification.getMessage().equals(String.format(MESSAGE_OS_APPROVED, serviceOrder.getId()))
        ));
    }

    @Test
    public void notifyManagersOutOfStock_withSuccess() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(1L);

        Employee manager1 = new Employee();
        manager1.setId(101L);

        Employee manager2 = new Employee();
        manager2.setId(102L);

        List<Employee> managers = new ArrayList<>();
        managers.add(manager1);
        managers.add(manager2);

        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MANAGER_DESCRIPTION))
                .thenReturn(managers);

        // Act
        notificationService.notifyManagersOutOfStock(serviceOrder);

        // Assert
        verify(notificationRepository, times(1)).save(Mockito.argThat(notification ->
                notification.getMessage().equals(String.format(MESSAGE_OUT_OF_STOCK, serviceOrder.getId())) &&
                        notification.getEmployees().containsAll(managers) &&
                        notification.getServiceOrder().equals(serviceOrder)
        ));
    }

    @Test
    public void notifyManagersOutOfStock_whenNoManagers() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(3L);  // Simulando um ID de OS

        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(MANAGER_DESCRIPTION))
                .thenReturn(new ArrayList<>());  // Nenhum gestor

        // Act
        notificationService.notifyManagersOutOfStock(serviceOrder);

        // Assert
        verify(notificationRepository, times(0)).save(Mockito.any());
    }

    @Test
    public void notifyAttendantsOSCompleted_withSuccess() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(2L);

        Employee attendant1 = new Employee();
        attendant1.setId(103L);

        Employee attendant2 = new Employee();
        attendant2.setId(104L);

        List<Employee> attendants = new ArrayList<>();
        attendants.add(attendant1);
        attendants.add(attendant2);

        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(ATTENDANT_DESCRIPTION))
                .thenReturn(attendants);

        // Act
        notificationService.notifyAttendantsOSCompleted(serviceOrder);

        // Assert
        verify(notificationRepository, times(1)).save(Mockito.argThat(notification ->
                notification.getMessage().equals(String.format(MESSAGE_OS_COMPLETED, serviceOrder.getId())) &&
                        notification.getEmployees().containsAll(attendants) &&
                        notification.getServiceOrder().equals(serviceOrder)
        ));
    }

    @Test
    public void notifyAttendantsOSCompleted_whenNoAttendants() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(4L);  // Simulando um ID de OS

        when(employeeRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(ATTENDANT_DESCRIPTION))
                .thenReturn(new ArrayList<>());  // Nenhum atendente

        // Act
        notificationService.notifyAttendantsOSCompleted(serviceOrder);

        // Assert
        verify(notificationRepository, times(0)).save(Mockito.any());
    }

}
