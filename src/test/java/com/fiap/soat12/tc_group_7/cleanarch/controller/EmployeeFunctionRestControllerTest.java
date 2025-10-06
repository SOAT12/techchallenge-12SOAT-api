package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.restcontroller.EmployeeFunctionRestController;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class EmployeeFunctionRestControllerTest {

    @Mock
    private EmployeeFunctionController employeeFunctionController;

    @InjectMocks
    private EmployeeFunctionRestController employeeFunctionRestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EmployeeFunctionRequestDTO getEmployeeFunctionRequestDTO() {
        EmployeeFunctionRequestDTO requestDTO = new EmployeeFunctionRequestDTO();
        requestDTO.setDescription("Engenheiro de Software");
        return requestDTO;
    }

    private EmployeeFunctionResponseDTO getEmployeeFunctionResponseDTO() {
        EmployeeFunctionResponseDTO responseDTO = new EmployeeFunctionResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setDescription("Engenheiro de Software");
        return responseDTO;
    }

    @Nested
    class CreateEmployeeFunction {

        @Test
        void shouldCreateEmployeeFunction() {
            // Arrange
            EmployeeFunctionRequestDTO requestDTO = getEmployeeFunctionRequestDTO();
            EmployeeFunctionResponseDTO responseDTO = getEmployeeFunctionResponseDTO();
            when(employeeFunctionController.createEmployeeFunction(requestDTO)).thenReturn(responseDTO);

            // Act
            EmployeeFunctionResponseDTO result = employeeFunctionRestController.create(requestDTO);

            // Assert
            assertNotNull(result);
            assertEquals(responseDTO, result);
            verify(employeeFunctionController, times(1)).createEmployeeFunction(requestDTO);
        }
    }

    @Nested
    class GetEmployeeFunction {

        @Test
        void shouldGetEmployeeFunctionById() {
            // Arrange
            Long id = 1L;
            EmployeeFunctionResponseDTO responseDTO = getEmployeeFunctionResponseDTO();
            when(employeeFunctionController.getEmployeeFunctionById(id)).thenReturn(responseDTO);

            // Act
            EmployeeFunctionResponseDTO result = employeeFunctionRestController.getById(id);

            // Assert
            assertNotNull(result);
            assertEquals(responseDTO, result);
            verify(employeeFunctionController, times(1)).getEmployeeFunctionById(id);
        }
    }

    @Nested
    class GetAllEmployeeFunctions {

        @Test
        void shouldGetAllActiveEmployeeFunctions() {
            // Arrange
            EmployeeFunctionResponseDTO responseDTO = getEmployeeFunctionResponseDTO();
            List<EmployeeFunctionResponseDTO> responseDTOList = Collections.singletonList(responseDTO);
            when(employeeFunctionController.getAllActiveEmployeeFunctions()).thenReturn(responseDTOList);

            // Act
            List<EmployeeFunctionResponseDTO> result = employeeFunctionRestController.getAllActiveEmployeeFunctions();

            // Assert
            assertNotNull(result);
            assertEquals(responseDTOList, result);
            verify(employeeFunctionController, times(1)).getAllActiveEmployeeFunctions();
        }

        @Test
        void shouldGetAllEmployeeFunctions() {
            // Arrange
            EmployeeFunctionResponseDTO responseDTO = getEmployeeFunctionResponseDTO();
            List<EmployeeFunctionResponseDTO> responseDTOList = Collections.singletonList(responseDTO);
            when(employeeFunctionController.getAllEmployeeFunctions()).thenReturn(responseDTOList);

            // Act
            List<EmployeeFunctionResponseDTO> result = employeeFunctionRestController.getAll();

            // Assert
            assertNotNull(result);
            assertEquals(responseDTOList, result);
            verify(employeeFunctionController, times(1)).getAllEmployeeFunctions();
        }
    }

    @Nested
    class UpdateEmployeeFunction {

        @Test
        void shouldUpdateEmployeeFunction() {
            // Arrange
            Long id = 1L;
            EmployeeFunctionRequestDTO requestDTO = getEmployeeFunctionRequestDTO();
            EmployeeFunctionResponseDTO responseDTO = getEmployeeFunctionResponseDTO();
            when(employeeFunctionController.updateEmployeeFunctionById(id, requestDTO)).thenReturn(responseDTO);

            // Act
            EmployeeFunctionResponseDTO result = employeeFunctionRestController.update(id, requestDTO);

            // Assert
            assertNotNull(result);
            assertEquals(responseDTO, result);
            verify(employeeFunctionController, times(1)).updateEmployeeFunctionById(id, requestDTO);
        }
    }

    @Nested
    class DeleteEmployeeFunction {

        @Test
        void shouldDeleteEmployeeFunction() {
            // Arrange
            Long id = 1L;
            doNothing().when(employeeFunctionController).inactivateEmployeeFunction(id);

            // Act
            employeeFunctionRestController.delete(id);

            // Assert
            verify(employeeFunctionController, times(1)).inactivateEmployeeFunction(id);
        }
    }

    @Nested
    class ActivateEmployeeFunction {

        @Test
        void shouldActivateEmployeeFunction() {
            // Arrange
            Long id = 1L;
            doNothing().when(employeeFunctionController).activateEmployeeFunction(id);

            // Act
            employeeFunctionRestController.activate(id);

            // Assert
            verify(employeeFunctionController, times(1)).activateEmployeeFunction(id);
        }
    }
}