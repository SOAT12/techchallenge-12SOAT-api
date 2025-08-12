package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.AverageExecutionTimeResponseDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ServiceOrderResponseDTO;
import com.fiap.soat12.tc_group_7.exception.InvalidTransitionException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.service.ServiceOrderService;
import com.fiap.soat12.tc_group_7.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ServiceOrderController Unit Tests")
class ServiceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ServiceOrderService serviceOrderService;

    private ServiceOrderRequestDTO requestDTO;
    private ServiceOrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new ServiceOrderRequestDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setVehicleId(1L);

        responseDTO = new ServiceOrderResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setStatus(Status.OPENED);
        responseDTO.setNotes("Test order");
    }

    @Nested
    @DisplayName("Endpoint: POST /api/service-orders")
    class CreateOrderTests {
        @Test
        @DisplayName("Should return 201 Created for a valid request")
        void createOrder_withValidRequest_shouldReturnCreated() throws Exception {
            when(serviceOrderService.createServiceOrder(any(ServiceOrderRequestDTO.class))).thenReturn(responseDTO);

            mockMvc.perform(post("/api/service-orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.status").value("OPENED"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request if service throws IllegalArgumentException")
        void createOrder_withInvalidData_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.createServiceOrder(any(ServiceOrderRequestDTO.class)))
                    .thenThrow(new IllegalArgumentException("Invalid input provided"));

            mockMvc.perform(post("/api/service-orders")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: PUT /api/service-orders/{id}")
    class UpdateOrderTests {
        @Test
        @DisplayName("Should return 200 OK when updating an existing order")
        void updateOrder_whenOrderExists_shouldReturnOk() throws Exception {
            when(serviceOrderService.updateOrder(eq(1L), any(ServiceOrderRequestDTO.class)))
                    .thenReturn(Optional.of(responseDTO));

            mockMvc.perform(put("/api/service-orders/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        @DisplayName("Should return 404 Not Found when trying to update a non-existent order")
        void updateOrder_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.updateOrder(eq(1L), any(ServiceOrderRequestDTO.class)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(put("/api/service-orders/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Should return 400 Bad Request when the service throws an IllegalArgumentException")
        void updateOrder_whenServiceThrowsError_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.updateOrder(eq(1L), any(ServiceOrderRequestDTO.class)))
                    .thenThrow(new IllegalArgumentException("Invalid data provided for update"));

            mockMvc.perform(put("/api/service-orders/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Endpoint: GET /api/service-orders/{id}")
    class GetOrderByIdTests {

        @Test
        @DisplayName("Should return 200 OK with the order data when ID exists")
        void getOrderById_whenOrderExists_shouldReturnOk() throws Exception {
            when(serviceOrderService.findById(1L)).thenReturn(responseDTO);

            mockMvc.perform(get("/api/service-orders/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.status", is("OPENED")))
                    .andExpect(jsonPath("$.notes", is("Test order")));
        }

        @Test
        @DisplayName("Should return 404 Not Found when ID does not exist")
        void getOrderById_whenOrderDoesNotExist_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.findById(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(get("/api/service-orders/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: GET /api/service-orders")
    class GetAllOrdersTests {

        @Test
        @DisplayName("Should return 200 OK with a list of orders when orders exist")
        void getAllOrders_whenOrdersExist_shouldReturnOkWithOrderList() throws Exception {
            ServiceOrderResponseDTO anotherResponseDTO = new ServiceOrderResponseDTO();
            anotherResponseDTO.setId(2L);
            anotherResponseDTO.setStatus(Status.APPROVED);

            when(serviceOrderService.findAllOrders()).thenReturn(List.of(responseDTO, anotherResponseDTO));

            mockMvc.perform(get("/api/service-orders"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[1].id", is(2)));
        }

        @Test
        @DisplayName("Should return 200 OK with an empty list when no orders exist")
        void getAllOrders_whenNoOrdersExist_shouldReturnOkWithEmptyList() throws Exception {
            when(serviceOrderService.findAllOrders()).thenReturn(Collections.emptyList());

            mockMvc.perform(get("/api/service-orders"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("Endpoint: DELETE /api/service-orders/{id}")
    class DeleteOrderTests {

        @Test
        @DisplayName("Should return 204 No Content when order exists and is logically deleted")
        void deleteOrder_whenOrderExists_shouldReturnNoContent() throws Exception {
            when(serviceOrderService.deleteOrderLogically(1L)).thenReturn(true);

            mockMvc.perform(delete("/api/service-orders/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Should return 404 Not Found when order ID does not exist")
        void deleteOrder_whenOrderDoesNotExist_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.deleteOrderLogically(99L)).thenReturn(false);

            mockMvc.perform(delete("/api/service-orders/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/approve")
    class ApproveTransitionTests {
        @Test
        @DisplayName("Should return 200 OK on a successful approval")
        void approve_whenSuccessful_shouldReturnOk() throws Exception {
            ServiceOrderResponseDTO approvedResponse = new ServiceOrderResponseDTO();
            approvedResponse.setId(1L);
            approvedResponse.setStatus(Status.APPROVED);

            when(serviceOrderService.approve(eq(1L), any())).thenReturn(Optional.of(approvedResponse));

            mockMvc.perform(patch("/api/service-orders/1/approve").param("employeeId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("APPROVED"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for an invalid transition")
        void approve_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.approve(eq(1L), any()))
                    .thenThrow(new InvalidTransitionException("Cannot approve from current state"));

            mockMvc.perform(patch("/api/service-orders/1/approve").param("employeeId", "1"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the order does not exist")
        void approve_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.approve(eq(99L), any())).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(patch("/api/service-orders/99/approve").param("employeeId", "1"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/diagnose")
    class DiagnoseTransitionTests {

        @Test
        @DisplayName("Should return 200 OK on a successful transition to In Diagnosis")
        void diagnose_whenSuccessful_shouldReturnOk() throws Exception {
            responseDTO.setStatus(Status.IN_DIAGNOSIS);
            when(serviceOrderService.diagnose(1L, 10L)).thenReturn(Optional.of(responseDTO));

            mockMvc.perform(patch("/api/service-orders/1/diagnose").param("employeeId", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("IN_DIAGNOSIS")));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for an invalid transition")
        void diagnose_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.diagnose(1L, 10L))
                    .thenThrow(new InvalidTransitionException("Cannot start diagnosis from current state"));

            mockMvc.perform(patch("/api/service-orders/1/diagnose").param("employeeId", "10"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the order does not exist")
        void diagnose_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.diagnose(99L, 10L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(patch("/api/service-orders/99/diagnose").param("employeeId", "10"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/reject")
    class RejectTransitionTests {

        @Test
        @DisplayName("Should return 200 OK on a successful rejection")
        void reject_whenSuccessful_shouldReturnOk() throws Exception {
            responseDTO.setStatus(Status.REJECTED);
            when(serviceOrderService.reject(1L, "Too expensive")).thenReturn(Optional.of(responseDTO));

            mockMvc.perform(patch("/api/service-orders/1/reject").param("reason", "Too expensive"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("REJECTED")));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for an invalid transition")
        void reject_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.reject(1L, "Too expensive"))
                    .thenThrow(new InvalidTransitionException("Cannot reject from current state"));

            mockMvc.perform(patch("/api/service-orders/1/reject").param("reason", "Too expensive"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the order does not exist")
        void reject_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.reject(99L, "Too expensive")).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(patch("/api/service-orders/99/reject").param("reason", "Too expensive"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/wait-for-approval")
    class WaitForApprovalTransitionTests {

        @Test
        @DisplayName("Should return 200 OK on a successful transition to Waiting for Approval")
        void waitForApproval_whenSuccessful_shouldReturnOk() throws Exception {
            responseDTO.setStatus(Status.WAITING_FOR_APPROVAL);
            when(serviceOrderService.waitForApproval(1L)).thenReturn(Optional.of(responseDTO));

            mockMvc.perform(patch("/api/service-orders/1/wait-for-approval"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("WAITING_FOR_APPROVAL")));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for an invalid transition")
        void waitForApproval_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.waitForApproval(1L))
                    .thenThrow(new InvalidTransitionException("Cannot transition from current state"));

            mockMvc.perform(patch("/api/service-orders/1/wait-for-approval"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the order does not exist")
        void waitForApproval_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.waitForApproval(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(patch("/api/service-orders/99/wait-for-approval"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/finish")
    class FinishTransitionTests {

        @Test
        @DisplayName("Should return 200 OK on a successful transition to Finished")
        void finish_whenSuccessful_shouldReturnOk() throws Exception {
            responseDTO.setStatus(Status.FINISHED);
            when(serviceOrderService.finish(1L)).thenReturn(Optional.of(responseDTO));

            mockMvc.perform(patch("/api/service-orders/1/finish"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("FINISHED")));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for an invalid transition")
        void finish_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.finish(1L))
                    .thenThrow(new InvalidTransitionException("Cannot finish from current state"));

            mockMvc.perform(patch("/api/service-orders/1/finish"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the order does not exist")
        void finish_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.finish(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(patch("/api/service-orders/99/finish"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Endpoint: PATCH /api/service-orders/{id}/deliver")
    class DeliverTransitionTests {

        @Test
        @DisplayName("Should return 200 OK on a successful transition to Delivered")
        void deliver_whenSuccessful_shouldReturnOk() throws Exception {
            responseDTO.setStatus(Status.DELIVERED);
            when(serviceOrderService.deliver(1L)).thenReturn(Optional.of(responseDTO));

            mockMvc.perform(patch("/api/service-orders/1/deliver"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status", is("DELIVERED")));
        }

        @Test
        @DisplayName("Should return 400 Bad Request for an invalid transition")
        void deliver_whenTransitionIsInvalid_shouldReturnBadRequest() throws Exception {
            when(serviceOrderService.deliver(1L))
                    .thenThrow(new InvalidTransitionException("Cannot deliver from current state"));

            mockMvc.perform(patch("/api/service-orders/1/deliver"))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 404 Not Found when the order does not exist")
        void deliver_whenOrderNotFound_shouldReturnNotFound() throws Exception {
            when(serviceOrderService.deliver(99L)).thenThrow(new NotFoundException("Ordem de Serviço não encontrada com o ID: 99"));

            mockMvc.perform(patch("/api/service-orders/99/deliver"))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    public void getAverageExecutionTime_withSuccess() throws Exception {
        AverageExecutionTimeResponseDTO dto = new AverageExecutionTimeResponseDTO(
                1L,
                "1 horas, 0 minutos"
        );

        when(serviceOrderService.calculateAverageExecutionTime(null, null, null))
                .thenReturn(dto);

        mockMvc.perform(get("/api/service-orders/average-execution-time"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageExecutionTimeHours").value(1))
                .andExpect(jsonPath("$.averageExecutionTimeFormatted").value("1 horas, 0 minutos"));
    }
}