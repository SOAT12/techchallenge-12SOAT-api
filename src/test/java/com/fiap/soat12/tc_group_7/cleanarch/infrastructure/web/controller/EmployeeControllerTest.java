package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeControllerTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeFunctionRepository employeeFunctionRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EmployeeJpaEntity getEmployeeJpaEntity() {
        EmployeeFunctionJpaEntity functionEntity = new EmployeeFunctionJpaEntity(1L, "Test", true);
        EmployeeJpaEntity entity = new EmployeeJpaEntity();
        entity.setId(1L);
        entity.setEmployeeFunction(functionEntity);
        return entity;
    }

    @Nested
    class CreateEmployee {
        @Test
        void shouldCreateEmployee() {
            // Arrange
            EmployeeRequestDTO dto = new EmployeeRequestDTO();
            dto.setEmployeeFunctionId(1L);
            dto.setPassword("password");

            when(employeeFunctionRepository.findById(any())).thenReturn(Optional.of(new EmployeeFunctionJpaEntity(1L, "Test", true)));
            when(employeeRepository.save(any())).thenReturn(getEmployeeJpaEntity());

            // Act
            EmployeeResponseDTO result = employeeController.createEmployee(dto);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class GetEmployeeById {
        @Test
        void shouldGetEmployeeById() {
            // Arrange
            Long id = 1L;
            when(employeeRepository.findById(id)).thenReturn(Optional.of(getEmployeeJpaEntity()));

            // Act
            EmployeeResponseDTO result = employeeController.getEmployeeById(id);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class GetAllEmployees {
        @Test
        void shouldGetAllEmployees() {
            // Arrange
            when(employeeRepository.findAll()).thenReturn(List.of(getEmployeeJpaEntity()));

            // Act
            List<EmployeeResponseDTO> result = employeeController.getAllEmployees();

            // Assert
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class GetAllActiveEmployees {
        @Test
        void shouldGetAllActiveEmployees() {
            // Arrange
            EmployeeJpaEntity entity = getEmployeeJpaEntity();
            entity.setActive(true);
            when(employeeRepository.findAll()).thenReturn(List.of(entity));

            // Act
            List<EmployeeResponseDTO> result = employeeController.getAllActiveEmployees();

            // Assert
            assertFalse(result.isEmpty());
        }
    }

    @Nested
    class UpdateEmployeeById {
        @Test
        void shouldUpdateEmployeeById() {
            // Arrange
            Long id = 1L;
            EmployeeRequestDTO dto = new EmployeeRequestDTO();
            dto.setEmployeeFunctionId(1L);

            when(employeeRepository.findById(id)).thenReturn(Optional.of(getEmployeeJpaEntity()));
            when(employeeFunctionRepository.findById(any())).thenReturn(Optional.of(new EmployeeFunctionJpaEntity(1L, "Test", true)));
            when(employeeRepository.save(any())).thenReturn(getEmployeeJpaEntity());

            // Act
            EmployeeResponseDTO result = employeeController.updateEmployeeById(id, dto);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class InactivateEmployee {
        @Test
        void shouldInactivateEmployee() {
            // Arrange
            Long id = 1L;
            when(employeeRepository.findById(id)).thenReturn(Optional.of(getEmployeeJpaEntity()));
            when(employeeRepository.save(any())).thenReturn(getEmployeeJpaEntity());

            // Act
            employeeController.inactivateEmployee(id);

            // Assert
            verify(employeeRepository).save(any());
        }
    }

    @Nested
    class ActivateEmployee {
        @Test
        void shouldActivateEmployee() {
            // Arrange
            Long id = 1L;
            when(employeeRepository.findById(id)).thenReturn(Optional.of(getEmployeeJpaEntity()));
            when(employeeRepository.save(any())).thenReturn(getEmployeeJpaEntity());

            // Act
            employeeController.activateEmployee(id);

            // Assert
            verify(employeeRepository).save(any());
        }
    }
}
