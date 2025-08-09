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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        Mockito.when(notificationRepository.findAll()).thenReturn(notifications);
        Mockito.when(notificationMapper.toNotificationResponseDTO(notification1)).thenReturn(dto1);
        Mockito.when(notificationMapper.toNotificationResponseDTO(notification2)).thenReturn(dto2);

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

        Mockito.when(notificationRepository.findByEmployees_Id(employeeId)).thenReturn(notifications);
        Mockito.when(notificationMapper.toNotificationResponseDTO(notification1)).thenReturn(dto1);

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
                .build();;

        Mockito.when(serviceOrderRepository.findById(requestDTO.getServiceOrderId())).thenReturn(Optional.of(serviceOrder));
        Mockito.when(employeeRepository.findAllById(requestDTO.getEmployeeIds())).thenReturn(new ArrayList<>(employees));
        Mockito.when(notificationRepository.save(Mockito.any())).thenReturn(notification);
        Mockito.when(notificationMapper.toNotificationResponseDTO(notification)).thenReturn(responseDTO);
        Mockito.when(notificationMapper.toNotification(Mockito.any(), Mockito.any(), Mockito.anySet())).thenReturn(notification);

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

        Mockito.when(serviceOrderRepository.findById(requestDTO.getServiceOrderId())).thenReturn(Optional.of(serviceOrder));
        Mockito.when(employeeRepository.findAllById(requestDTO.getEmployeeIds())).thenReturn(new ArrayList<>(employees));

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

        Mockito.when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        Mockito.doNothing().when(notificationRepository).delete(notification);

        // Act
        notificationService.deleteNotification(notificationId);

        // Assert
        Mockito.verify(notificationRepository, Mockito.times(1)).delete(notification);
    }

    @Test
    void deleteNotification_withNotFoundException() {
        // Arrange
        Long notificationId = 50L;

        Mockito.when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> notificationService.deleteNotification(notificationId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Notificação não encontrada");
    }

}
