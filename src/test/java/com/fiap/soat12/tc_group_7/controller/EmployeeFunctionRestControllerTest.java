package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.service.EmployeeFunctionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EmployeeFunctionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeFunctionService employeeFunctionService;

    @Test
    @DisplayName("POST /api/employee-functions - Deve criar uma nova função de funcionário com sucesso")
    void shouldCreateEmployeeFunction() throws Exception {
        EmployeeFunctionRequestDTO requestDTO = EmployeeFunctionRequestDTO.builder()
                .description("Gerente")
                .build();
        EmployeeFunctionResponseDTO responseDTO = EmployeeFunctionResponseDTO.builder()
                .id(1L)
                .description("Gerente")
                .active(true)
                .build();

        when(employeeFunctionService.createEmployeeFunction(any(EmployeeFunctionRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/employee-functions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Gerente"));
    }

    @Test
    @DisplayName("GET /api/employee-functions/{id} - Deve retornar função de funcionário pelo ID")
    void shouldGetEmployeeFunctionById() throws Exception {
        EmployeeFunctionResponseDTO responseDTO = EmployeeFunctionResponseDTO.builder()
                .id(1L)
                .description("Gerente")
                .active(true)
                .build();

        when(employeeFunctionService.getEmployeeFunctionById(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/employee-functions/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Gerente"));
    }

    @Test
    @DisplayName("GET /api/employee-functions/{id} - Deve retornar 404 Not Found se a função não for encontrada")
    void shouldReturnNotFoundWhenEmployeeFunctionNotFoundById() throws Exception {
        when(employeeFunctionService.getEmployeeFunctionById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employee-functions/{id}", 99L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/employee-functions - Deve listar todas as funções de funcionário ativas")
    void shouldGetAllActiveEmployeeFunctions() throws Exception {
        List<EmployeeFunctionResponseDTO> responseList = Arrays.asList(
                EmployeeFunctionResponseDTO.builder().id(1L).description("Gerente").active(true).build(),
                EmployeeFunctionResponseDTO.builder().id(2L).description("Analista").active(true).build()
        );

        when(employeeFunctionService.getAllEmployeeFunctions()).thenReturn(responseList);

        mockMvc.perform(get("/api/employee-functions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Gerente"))
                .andExpect(jsonPath("$[1].description").value("Analista"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/employee-functions/all - Deve listar todas as funções de funcionário, incluindo inativas")
    void shouldGetAllEmployeeFunctionsIncludingInactive() throws Exception {
        List<EmployeeFunctionResponseDTO> responseList = Arrays.asList(
                EmployeeFunctionResponseDTO.builder().id(1L).description("Gerente").active(true).build(),
                EmployeeFunctionResponseDTO.builder().id(2L).description("Ex-Gerente").active(false).build()
        );

        when(employeeFunctionService.getAllEmployeeFunctionsIncludingInactive()).thenReturn(responseList);

        mockMvc.perform(get("/api/employee-functions/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT /api/employee-functions/{id} - Deve atualizar uma função de funcionário com sucesso")
    void shouldUpdateEmployeeFunction() throws Exception {
        EmployeeFunctionRequestDTO requestDTO = EmployeeFunctionRequestDTO.builder()
                .description("Analista")
                .active(true)
                .build();
        EmployeeFunctionResponseDTO responseDTO = EmployeeFunctionResponseDTO.builder()
                .id(1L)
                .description("Analista")
                .active(true)
                .build();

        when(employeeFunctionService.updateEmployeeFunction(eq(1L), any(EmployeeFunctionRequestDTO.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/employee-functions/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("Analista"));
    }

    @Test
    @DisplayName("PUT /api/employee-functions/{id} - Deve retornar 404 Not Found se a função a ser atualizada não for encontrada")
    void shouldReturnNotFoundWhenUpdatingNonExistentEmployeeFunction() throws Exception {
        EmployeeFunctionRequestDTO requestDTO = EmployeeFunctionRequestDTO.builder()
                .description("Analista")
                .build();

        when(employeeFunctionService.updateEmployeeFunction(eq(99L), any(EmployeeFunctionRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employee-functions/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/employee-functions/{id} - Deve inativar uma função de funcionário com sucesso")
    void shouldInactivateEmployeeFunction() throws Exception {
        when(employeeFunctionService.inactivateEmployeeFunction(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/employee-functions/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/employee-functions/{id} - Deve retornar 404 Not Found se a função a ser inativada não for encontrada")
    void shouldReturnNotFoundWhenInactivatingNonExistentEmployeeFunction() throws Exception {
        when(employeeFunctionService.inactivateEmployeeFunction(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/employee-functions/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/employee-functions/{id}/activate - Deve reativar uma função de funcionário com sucesso")
    void shouldActivateEmployeeFunction() throws Exception {
        when(employeeFunctionService.activateEmployeeFunction(1L)).thenReturn(true);

        mockMvc.perform(put("/api/employee-functions/{id}/activate", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/employee-functions/{id}/activate - Deve retornar 404 Not Found se a função a ser reativada não for encontrada")
    void shouldReturnNotFoundWhenActivatingNonExistentEmployeeFunction() throws Exception {
        when(employeeFunctionService.activateEmployeeFunction(99L)).thenReturn(false);

        mockMvc.perform(put("/api/employee-functions/{id}/activate", 99L))
                .andExpect(status().isNotFound());
    }
}

