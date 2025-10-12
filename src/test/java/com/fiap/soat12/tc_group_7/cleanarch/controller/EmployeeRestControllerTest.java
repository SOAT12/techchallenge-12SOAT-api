package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.service.AuthEmployeeService;
import com.fiap.soat12.tc_group_7.service.EmployeeService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EmployeeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private AuthEmployeeService authEmployeeService;

    @MockitoBean
    private EmployeeController employeeController;


    private EmployeeRequestDTO getEmployeeRequestDTO() {
        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
        requestDTO.setCpf("12345678900");
        requestDTO.setName("Funcionario Teste");
        requestDTO.setEmail("teste@teste.com");
        requestDTO.setEmployeeFunctionId(1L);
        requestDTO.setPassword("password123");
        requestDTO.setPhone("11999999999");
        return requestDTO;
    }

    private EmployeeResponseDTO getEmployeeResponseDTO() {
        EmployeeResponseDTO responseDTO = new EmployeeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCpf("12345678900");
        responseDTO.setName("Funcionario Teste");
        responseDTO.setEmail("teste@teste.com");
        return responseDTO;
    }

//    @Test
//    void shouldLoginSuccessfully() throws Exception {
//        LoginRequestDTO requestDTO = new LoginRequestDTO("12345678900", "senha123");
//        LoginResponseDTO responseDTO = new LoginResponseDTO("mock-token-jwt");
//
//        when(authEmployeeService.auth(any(LoginRequestDTO.class))).thenReturn(responseDTO);
//
//        mockMvc.perform(post("/clean-arch/employees/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value("mock-token-jwt"));
//    }
//
//    @Test
//    void shouldReturnUnauthorizedForInvalidLoginCredentials() throws Exception {
//        LoginRequestDTO requestDTO = new LoginRequestDTO("12345678900", "senhaerrada");
//
//        when(authEmployeeService.auth(any(LoginRequestDTO.class)))
//                .thenThrow(new BadCredentialsException("INVALID_CREDENTIALS"));
//
//        mockMvc.perform(post("/clean-arch/employees/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void shouldChangePasswordSuccessfully() throws Exception {
//        Long employeeId = 101L;
//        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("senhaAntiga", "senhaNova", "senhaNova");
//
//        doNothing().when(employeeService).changePassword(eq(employeeId), any(ChangePasswordRequestDTO.class));
//
//        mockMvc.perform(put("/clean-arch/employees/{id}/change-password", employeeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenNewPasswordDoesNotMatchConfirmation() throws Exception {
//        Long employeeId = 101L;
//        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("senhaAntiga", "senhaNova", "senhaDiferente");
//
//        doThrow(new BadCredentialsException("A nova senha e a confirmação não são iguais."))
//                .when(employeeService).changePassword(eq(employeeId), any(ChangePasswordRequestDTO.class));
//
//        mockMvc.perform(put("/clean-arch/employees/{id}/change-password", employeeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenOldPasswordIsIncorrect() throws Exception {
//        Long employeeId = 101L;
//        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("senhaAntigaErrada", "senhaNova", "senhaNova");
//
//        doThrow(new BadCredentialsException("A senha antiga está incorreta."))
//                .when(employeeService).changePassword(eq(employeeId), any(ChangePasswordRequestDTO.class));
//
//        mockMvc.perform(put("/clean-arch/employees/{id}/change-password", employeeId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isUnauthorized());
//    }
//
//
//    @Test
//    void shouldSendForgotPasswordEmailSuccessfully() throws Exception {
//        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("12345678900");
//
//        doNothing().when(employeeService).forgotPassword(any(ForgotPasswordRequestDTO.class));
//
//        mockMvc.perform(post("/clean-arch/employees/forgot-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void shouldReturnBadRequestWhenEmployeeNotFoundOnForgotPassword() throws Exception {
//        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("99999999999");
//
//        doThrow(new UsernameNotFoundException("FALHA NA IDENTIFICAÇÃO: 99999999999"))
//                .when(employeeService).forgotPassword(any(ForgotPasswordRequestDTO.class));
//
//        mockMvc.perform(post("/clean-arch/employees/forgot-password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Nested
//    class CreateEmployee {
//        @Test
//        void shouldCreateEmployee() throws Exception {
//            // Arrange
//            EmployeeRequestDTO requestDTO = getEmployeeRequestDTO();
//            EmployeeResponseDTO responseDTO = getEmployeeResponseDTO();
//            when(employeeController.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(responseDTO);
//
//            // Act & Assert
//            mockMvc.perform(post("/clean-arch/employees")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(responseDTO.getId()));
//        }
//
//        @Test
//        void shouldReturnBadRequestWhenCreateEmployeeWithInvalidData() throws Exception {
//            // Arrange
//            EmployeeRequestDTO requestDTO = getEmployeeRequestDTO();
//            when(employeeController.createEmployee(any(EmployeeRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));
//
//            // Act & Assert
//            mockMvc.perform(post("/clean-arch/employees")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    @Nested
//    class GetEmployee {
//        @Test
//        void shouldGetEmployeeById() throws Exception {
//            // Arrange
//            EmployeeResponseDTO responseDTO = getEmployeeResponseDTO();
//            when(employeeController.getEmployeeById(anyLong())).thenReturn(responseDTO);
//
//            // Act & Assert
//            mockMvc.perform(get("/clean-arch/employees/{id}", 1L))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(responseDTO.getId()));
//        }
//
//        @Test
//        void shouldGetAllEmployees() throws Exception {
//            // Arrange
//            List<EmployeeResponseDTO> response = Collections.singletonList(getEmployeeResponseDTO());
//            when(employeeController.getAllEmployees()).thenReturn(response);
//
//            // Act & Assert
//            mockMvc.perform(get("/clean-arch/employees/all"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$[0].id").value(response.get(0).getId()));
//        }
//
//        @Test
//        void shouldGetAllActiveEmployees() throws Exception {
//            // Arrange
//            List<EmployeeResponseDTO> response = Collections.singletonList(getEmployeeResponseDTO());
//            when(employeeController.getAllActiveEmployees()).thenReturn(response);
//
//            // Act & Assert
//            mockMvc.perform(get("/clean-arch/employees"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$[0].id").value(response.get(0).getId()));
//        }
//    }
//
//    @Nested
//    class UpdateEmployee {
//        @Test
//        void shouldUpdateEmployee() throws Exception {
//            // Arrange
//            EmployeeRequestDTO requestDTO = getEmployeeRequestDTO();
//            EmployeeResponseDTO responseDTO = getEmployeeResponseDTO();
//            when(employeeController.updateEmployeeById(anyLong(), any(EmployeeRequestDTO.class))).thenReturn(responseDTO);
//
//            // Act & Assert
//            mockMvc.perform(put("/clean-arch/employees/{id}", 1L)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(responseDTO.getId()));
//        }
//
//        @Test
//        void shouldReturnBadRequestWhenUpdateEmployeeWithInvalidData() throws Exception {
//            // Arrange
//            EmployeeRequestDTO requestDTO = getEmployeeRequestDTO();
//            when(employeeController.updateEmployeeById(anyLong(), any(EmployeeRequestDTO.class))).thenThrow(new IllegalArgumentException("Invalid data"));
//
//            // Act & Assert
//            mockMvc.perform(put("/clean-arch/employees/{id}", 1L)
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(requestDTO)))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    @Nested
//    class DeleteEmployee {
//        @Test
//        void shouldDeleteEmployee() throws Exception {
//            // Arrange
//            doNothing().when(employeeController).inactivateEmployee(anyLong());
//
//            // Act & Assert
//            mockMvc.perform(delete("/clean-arch/employees/{id}", 1L))
//                    .andExpect(status().isOk());
//        }
//    }
//
//    @Nested
//    class ActivateEmployee {
//        @Test
//        void shouldActivateEmployee() throws Exception {
//            // Arrange
//            doNothing().when(employeeController).activateEmployee(anyLong());
//
//            // Act & Assert
//            mockMvc.perform(put("/clean-arch/employees/{id}/activate", 1L))
//                    .andExpect(status().isOk());
//        }
//    }
}

