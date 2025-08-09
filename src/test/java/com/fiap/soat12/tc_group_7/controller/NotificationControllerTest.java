package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationRequestDTO;
import com.fiap.soat12.tc_group_7.dto.notification.NotificationResponseDTO;
import com.fiap.soat12.tc_group_7.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationService notificationService;

    @Test
    void getAllNotifications_withSuccess() throws Exception {
        List<NotificationResponseDTO> mockList = List.of(
                NotificationResponseDTO.builder()
                        .id(1L)
                        .message("Mensagem 1")
                        .isRead(false)
                        .build(),
                NotificationResponseDTO.builder()
                        .id(2L)
                        .message("Mensagem 2")
                        .isRead(true)
                        .build()
        );

        Mockito.when(notificationService.getAllNotifications()).thenReturn(mockList);

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].message").value("Mensagem 1"))
                .andExpect(jsonPath("$[0].isRead").value(false));
    }

    @Test
    void getNotificationsByEmployeeId_withSuccess() throws Exception {
        Long employeeId = 123L;

        List<NotificationResponseDTO> mockList = List.of(
                NotificationResponseDTO.builder()
                        .id(10L)
                        .message("Notificação para funcionário")
                        .isRead(true)
                        .build()
        );

        Mockito.when(notificationService.getNotificationsByEmployeeId(employeeId)).thenReturn(mockList);

        mockMvc.perform(get("/api/notifications/by-employee")
                        .param("employeeId", employeeId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(10L));
    }

    @Test
    void createNotification_withSuccess() throws Exception {
        NotificationRequestDTO requestDTO = NotificationRequestDTO.builder()
                .message("Nova notificação")
                .serviceOrderId(99L)
                .employeeIds(Set.of(1L, 2L))
                .build();
        NotificationResponseDTO responseDTO = NotificationResponseDTO.builder()
                .id(50L)
                .message("Nova notificação")
                .isRead(false)
                .build();

        Mockito.when(notificationService.createNotification(any())).thenReturn(responseDTO);

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(50L))
                .andExpect(jsonPath("$.message").value("Nova notificação"));
    }

    @Test
    void deleteNotification_withSuccess() throws Exception {
        Long notificationId = 77L;

        Mockito.doNothing().when(notificationService).deleteNotification(notificationId);

        mockMvc.perform(delete("/api/notifications/{id}", notificationId))
                .andExpect(status().isOk());
    }

}
