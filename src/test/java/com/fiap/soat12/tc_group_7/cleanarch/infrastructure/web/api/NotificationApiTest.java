package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.NotificationController;
import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NotificationApiTest {

    @Mock
    private NotificationController notificationController;

    @InjectMocks
    private NotificationApi notificationApi; // Nome da classe presumido

    private NotificationRequestDTO sampleRequest;
    private NotificationResponseDTO sampleResponse;

    @BeforeEach
    void setup() {
        sampleRequest = NotificationRequestDTO.builder()
                .message("Nova ordem de serviço disponível")
                .serviceOrderId(10L)
                .employeeIds(Set.of(1L, 2L))
                .build();

        NotificationResponseDTO.ServiceOrderDTO serviceOrderDTO = NotificationResponseDTO.ServiceOrderDTO.builder()
                .id(10L)
                .status(Status.OPENED)
                .totalValue(new BigDecimal("250.00"))
                .createdAt(new Date())
                .finishedAt(null)
                .build();

        NotificationResponseDTO.EmployeeDTO employee1 = NotificationResponseDTO.EmployeeDTO.builder()
                .id(1L)
                .name("João Silva")
                .build();

        NotificationResponseDTO.EmployeeDTO employee2 = NotificationResponseDTO.EmployeeDTO.builder()
                .id(2L)
                .name("Maria Oliveira")
                .build();

        sampleResponse = NotificationResponseDTO.builder()
                .id(100L)
                .message("Nova ordem de serviço disponível")
                .isRead(false)
                .serviceOrder(serviceOrderDTO)
                .employees(List.of(employee1, employee2))
                .build();
    }

    @Test
    void getAllNotifications_shouldReturnList() {
        when(notificationController.getAllNotifications()).thenReturn(List.of(sampleResponse));

        List<NotificationResponseDTO> result = notificationApi.getAllNotifications();

        assertEquals(1, result.size());
        assertEquals("Nova ordem de serviço disponível", result.get(0).getMessage());
        verify(notificationController).getAllNotifications();
    }

    @Test
    void getNotificationsByEmployeeId_shouldReturnList() {
        Long employeeId = 1L;
        when(notificationController.getNotificationsByEmployeeId(employeeId)).thenReturn(List.of(sampleResponse));

        List<NotificationResponseDTO> result = notificationApi.getNotificationsByEmployeeId(employeeId);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getEmployees().size());
        verify(notificationController).getNotificationsByEmployeeId(employeeId);
    }

    @Test
    void createNotification_shouldReturnCreatedNotification() {
        when(notificationController.createNotification(sampleRequest)).thenReturn(sampleResponse);

        NotificationResponseDTO result = notificationApi.createNotification(sampleRequest);

        assertNotNull(result);
        assertEquals("Nova ordem de serviço disponível", result.getMessage());
        verify(notificationController).createNotification(sampleRequest);
    }

    @Test
    void deleteNotification_shouldCallController() {
        Long id = 100L;

        notificationApi.deleteNotification(id);

        verify(notificationController).deleteNotification(id);
    }

}
