package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    private final EmployeeFunctionResponseDTO functionResponseDTO = EmployeeFunctionResponseDTO.builder()
            .id(1L)
            .description("Gerente")
            .build();

    @Test
    @DisplayName("POST /api/employees - Deve criar um novo funcionário com sucesso")
    void shouldCreateEmployee() throws Exception {
        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();
        EmployeeResponseDTO responseDTO = EmployeeResponseDTO.builder()
                .id(101L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(functionResponseDTO)
                .build();

        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.employeeFunction.description").value("Gerente"));
    }

    @Test
    @DisplayName("POST /api/employees - Deve retornar 400 Bad Request se a função não for encontrada")
    void shouldReturnBadRequestWhenFunctionNotFoundOnCreate() throws Exception {
        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(99L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();

        when(employeeService.createEmployee(any(EmployeeRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Função não encontrada"));

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Deve retornar funcionário pelo ID")
    void shouldGetEmployeeById() throws Exception {
        EmployeeResponseDTO responseDTO = EmployeeResponseDTO.builder()
                .id(101L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(functionResponseDTO)
                .build();

        when(employeeService.getEmployeeById(101L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/employees/{id}", 101L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("GET /api/employees/{id} - Deve retornar 404 Not Found se o funcionário não for encontrado")
    void shouldReturnNotFoundWhenEmployeeNotFoundById() throws Exception {
        when(employeeService.getEmployeeById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/{id}", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/employees/all - Deve listar todos os funcionários")
    void shouldGetAllEmployees() throws Exception {
        List<EmployeeResponseDTO> responseList = Arrays.asList(
                EmployeeResponseDTO.builder().id(101L).cpf("123.456.789-00").name("Maria Souza").active(true).employeeFunction(functionResponseDTO).build(),
                EmployeeResponseDTO.builder().id(102L).cpf("987.654.321-00").name("João Silva").active(true).employeeFunction(functionResponseDTO).build()
        );

        when(employeeService.getAllEmployees()).thenReturn(responseList);

        mockMvc.perform(get("/api/employees/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[1].cpf").value("987.654.321-00"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("GET /api/employees - Deve listar todos os funcionários ativos")
    void shouldGetAllActiveEmployees() throws Exception {
        List<EmployeeResponseDTO> responseList = Arrays.asList(
                EmployeeResponseDTO.builder().id(101L).cpf("123.456.789-00").name("Maria Souza").active(true).employeeFunction(functionResponseDTO).build(),
                EmployeeResponseDTO.builder().id(102L).cpf("987.654.321-00").name("João Silva").active(true).employeeFunction(functionResponseDTO).build()
        );

        when(employeeService.getAllActiveEmployees()).thenReturn(responseList);

        mockMvc.perform(get("/api/employees")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[1].cpf").value("987.654.321-00"))
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Deve atualizar um funcionário com sucesso")
    void shouldUpdateEmployee() throws Exception {
        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();
        EmployeeResponseDTO responseDTO = EmployeeResponseDTO.builder()
                .id(101L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(functionResponseDTO)
                .build();

        when(employeeService.updateEmployee(eq(101L), any(EmployeeRequestDTO.class))).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(put("/api/employees/{id}", 101L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Deve retornar 404 Not Found se o funcionário a ser atualizado não for encontrado")
    void shouldReturnNotFoundWhenUpdatingNonExistentEmployee() throws Exception {
        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();

        when(employeeService.updateEmployee(eq(999L), any(EmployeeRequestDTO.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/employees/{id} - Deve retornar 400 Bad Request se a função não for encontrada ao atualizar")
    void shouldReturnBadRequestWhenFunctionNotFoundOnUpdate() throws Exception {
        EmployeeRequestDTO requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(99L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();

        when(employeeService.updateEmployee(eq(101L), any(EmployeeRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("Função não encontrada"));

        mockMvc.perform(put("/api/employees/{id}", 101L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Deve deletar logicamente um funcionário com sucesso")
    void shouldLogicallyDeleteEmployee() throws Exception {
        when(employeeService.inactivateEmployee(101L)).thenReturn(true);

        mockMvc.perform(delete("/api/employees/{id}", 101L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} - Deve retornar 404 Not Found se o funcionário a ser deletado não for encontrado")
    void shouldReturnNotFoundWhenLogicallyDeletingNonExistentEmployee() throws Exception {
        when(employeeService.inactivateEmployee(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/employees/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/employees/{id}/activate - Deve reativar um funcionário com sucesso")
    void shouldActivateEmployee() throws Exception {
        when(employeeService.activateEmployee(101L)).thenReturn(true);

        mockMvc.perform(put("/api/employees/{id}/activate", 101L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /api/employees/{id}/activate - Deve retornar 404 Not Found se o funcionário a ser reativado não for encontrado")
    void shouldReturnNotFoundWhenActivatingNonExistentEmployee() throws Exception {
        when(employeeService.activateEmployee(999L)).thenReturn(false);

        mockMvc.perform(put("/api/employees/{id}/activate", 999L))
                .andExpect(status().isNotFound());
    }
}

