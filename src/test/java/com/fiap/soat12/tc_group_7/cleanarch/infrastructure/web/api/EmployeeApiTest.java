package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.EmployeeController;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeApiTest {

    @Mock
    private EmployeeController employeeController;

    @InjectMocks
    private EmployeeApi employeeApi;

    private EmployeeRequestDTO sampleRequest;
    private EmployeeResponseDTO sampleResponse;

    @BeforeEach
    void setup() {
        sampleRequest = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("12345678900")
                .name("Maria Souza")
                .password("senha123")
                .phone("11999999999")
                .email("maria@example.com")
                .active(true)
                .build();

        sampleResponse = EmployeeResponseDTO.builder()
                .id(1L)
                .cpf(sampleRequest.getCpf())
                .name(sampleRequest.getName())
                .phone(sampleRequest.getPhone())
                .email(sampleRequest.getEmail())
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(EmployeeFunctionResponseDTO.builder()
                        .id(1L)
                        .description("Gerente")
                        .build())
                .build();
    }

    @Test
    void createEmployee_shouldReturnCreatedEmployee() {
        when(employeeController.createEmployee(sampleRequest)).thenReturn(sampleResponse);

        EmployeeResponseDTO result = employeeApi.createEmployee(sampleRequest);

        assertNotNull(result);
        assertEquals("Maria Souza", result.getName());
        verify(employeeController).createEmployee(sampleRequest);
    }

    @Test
    void createEmployee_shouldThrowBadRequest_onIllegalArgumentException() {
        when(employeeController.createEmployee(sampleRequest))
                .thenThrow(new IllegalArgumentException("CPF inválido"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            employeeApi.createEmployee(sampleRequest);
        });

        assertEquals("400 BAD_REQUEST \"CPF inválido\"", exception.getMessage());
        verify(employeeController).createEmployee(sampleRequest);
    }

    @Test
    void getEmployeeById_shouldReturnEmployee() {
        when(employeeController.getEmployeeById(1L)).thenReturn(sampleResponse);

        EmployeeResponseDTO result = employeeApi.getEmployeeById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Maria Souza", result.getName());
        verify(employeeController).getEmployeeById(1L);
    }

    @Test
    void getAllEmployees_shouldReturnList() {
        when(employeeController.getAllEmployees()).thenReturn(List.of(sampleResponse));

        List<EmployeeResponseDTO> result = employeeApi.getAllEmployees();

        assertEquals(1, result.size());
        assertEquals("Maria Souza", result.get(0).getName());
        verify(employeeController).getAllEmployees();
    }

    @Test
    void getAllActiveEmployees_shouldReturnList() {
        when(employeeController.getAllActiveEmployees()).thenReturn(List.of(sampleResponse));

        List<EmployeeResponseDTO> result = employeeApi.getAllActiveEmployees();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
        verify(employeeController).getAllActiveEmployees();
    }

    @Test
    void updateEmployeeById_shouldReturnUpdatedEmployee() {
        EmployeeRequestDTO updatedRequest = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("12345678900")
                .name("Maria Atualizada")
                .password("novaSenha")
                .phone("11988888888")
                .email("maria.nova@example.com")
                .active(true)
                .build();

        EmployeeResponseDTO updatedResponse = EmployeeResponseDTO.builder()
                .id(1L)
                .cpf(updatedRequest.getCpf())
                .name(updatedRequest.getName())
                .phone(updatedRequest.getPhone())
                .email(updatedRequest.getEmail())
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(EmployeeFunctionResponseDTO.builder().id(1L).description("Gerente").build())
                .build();

        when(employeeController.updateEmployeeById(1L, updatedRequest)).thenReturn(updatedResponse);

        EmployeeResponseDTO result = employeeApi.updateEmployeeById(1L, updatedRequest);

        assertEquals("Maria Atualizada", result.getName());
        assertEquals("maria.nova@example.com", result.getEmail());
        verify(employeeController).updateEmployeeById(1L, updatedRequest);
    }

    @Test
    void updateEmployeeById_shouldThrowBadRequest_onIllegalArgumentException() {
        when(employeeController.updateEmployeeById(eq(1L), any()))
                .thenThrow(new IllegalArgumentException("Dados inválidos"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            employeeApi.updateEmployeeById(1L, sampleRequest);
        });

        assertEquals("400 BAD_REQUEST \"Dados inválidos\"", exception.getMessage());
        verify(employeeController).updateEmployeeById(1L, sampleRequest);
    }

    @Test
    void deleteEmployee_shouldCallController() {
        employeeApi.deleteEmployee(1L);
        verify(employeeController).inactivateEmployee(1L);
    }

    @Test
    void activateEmployee_shouldCallController() {
        employeeApi.activateEmployee(1L);
        verify(employeeController).activateEmployee(1L);
    }
}