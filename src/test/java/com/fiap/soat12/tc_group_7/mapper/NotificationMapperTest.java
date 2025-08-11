package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.Notification;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class NotificationMapperTest {

    @InjectMocks
    private NotificationMapper notificationMapper;

    @Test
    void toNotificationResponseDTO_withSuccess() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(1L);
        serviceOrder.setTotalValue(BigDecimal.valueOf(100.0));

        Employee employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Employee 1");

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Employee 2");

        Set<Employee> employees = Set.of(employee1, employee2);

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setMessage("Nova notificação");
        notification.setRead(false);
        notification.setServiceOrder(serviceOrder);
        notification.setEmployees(employees);

        // Act
        NotificationResponseDTO result = notificationMapper.toNotificationResponseDTO(notification);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getMessage()).isEqualTo("Nova notificação");
        assertThat(result.getServiceOrder()).isNotNull();
        assertThat(result.getServiceOrder().getId()).isEqualTo(1L);
        assertThat(result.getEmployees()).hasSize(2);
    }

    @Test
    void toNotification_withSuccess() {
        // Arrange
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId(1L);

        Employee employee1 = new Employee();
        employee1.setId(1L);

        Employee employee2 = new Employee();
        employee2.setId(2L);

        Set<Employee> employees = Set.of(employee1, employee2);

        NotificationRequestDTO requestDTO = NotificationRequestDTO.builder()
                .message("Nova notificação")
                .serviceOrderId(1L)
                .employeeIds(Set.of(1L, 2L))
                .build();

        // Act
        Notification result = notificationMapper.toNotification(requestDTO, serviceOrder, employees);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Nova notificação");
        assertThat(result.isRead()).isFalse();
        assertThat(result.getServiceOrder()).isEqualTo(serviceOrder);
        assertThat(result.getEmployees()).isEqualTo(employees);
    }

}
