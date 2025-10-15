package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeFunctionGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.EmployeeFunctionPresenter;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeFunctionUseCaseTest {

    @Mock
    private EmployeeFunctionGateway employeeFunctionGateway;

    @Mock
    private EmployeeFunctionPresenter employeeFunctionPresenter;

    @InjectMocks
    private EmployeeFunctionUseCase employeeFunctionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class GetAllEmployeeFunctions {
        @Test
        void shouldReturnAllEmployeeFunctions() {
            // Arrange
            when(employeeFunctionGateway.findAll()).thenReturn(List.of(EmployeeFunction.builder().build()));

            // Act
            List<EmployeeFunction> result = employeeFunctionUseCase.getAllEmployeeFunctions();

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeFunctionGateway).findAll();
        }
    }

    @Nested
    class GetAllActiveEmployeeFunctions {
        @Test
        void shouldReturnAllActiveEmployeeFunctions() {
            // Arrange
            EmployeeFunction activeFunction = EmployeeFunction.builder().active(true).build();
            EmployeeFunction inactiveFunction = EmployeeFunction.builder().active(false).build();
            when(employeeFunctionGateway.findAll()).thenReturn(List.of(activeFunction, inactiveFunction));

            // Act
            List<EmployeeFunction> result = employeeFunctionUseCase.getAllActiveEmployeeFunctions();

            // Assert
            assertEquals(1, result.size());
            assertTrue(result.get(0).getActive());
            verify(employeeFunctionGateway).findAll();
        }
    }

    @Nested
    class GetEmployeeFunctionById {
        @Test
        void shouldReturnEmployeeFunctionWhenFound() {
            // Arrange
            Long id = 1L;
            EmployeeFunction function = EmployeeFunction.builder().id(id).build();
            when(employeeFunctionGateway.findById(id)).thenReturn(Optional.of(function));

            // Act
            Optional<EmployeeFunction> result = employeeFunctionUseCase.getEmployeeFunctionById(id);

            // Assert
            assertTrue(result.isPresent());
            assertEquals(id, result.get().getId());
            verify(employeeFunctionGateway).findById(id);
        }
    }

    @Nested
    class CreateEmployeeFunction {
        @Test
        void shouldCreateEmployeeFunction() {
            // Arrange
            EmployeeFunctionRequestDTO dto = new EmployeeFunctionRequestDTO();
            EmployeeFunction function = EmployeeFunction.builder().build();
            when(employeeFunctionPresenter.toEmployeeFunction(dto)).thenReturn(function);
            when(employeeFunctionGateway.save(function)).thenReturn(function);

            // Act
            EmployeeFunction result = employeeFunctionUseCase.createEmployeeFunction(dto);

            // Assert
            assertNotNull(result);
            assertTrue(result.getActive());
            verify(employeeFunctionGateway).save(function);
        }
    }

    @Nested
    class UpdateEmployeeFunction {
        @Test
        void shouldUpdateEmployeeFunction() {
            // Arrange
            Long id = 1L;
            EmployeeFunctionRequestDTO dto = new EmployeeFunctionRequestDTO();
            dto.setDescription("New Description");
            EmployeeFunction existingFunction = EmployeeFunction.builder().id(id).description("Old").build();
            when(employeeFunctionGateway.findById(id)).thenReturn(Optional.of(existingFunction));
            when(employeeFunctionGateway.save(existingFunction)).thenReturn(existingFunction);

            // Act
            Optional<EmployeeFunction> result = employeeFunctionUseCase.updateEmployeeFunction(id, dto);

            // Assert
            assertTrue(result.isPresent());
            assertEquals("New Description", result.get().getDescription());
            verify(employeeFunctionGateway).save(existingFunction);
        }
    }

    @Nested
    class InactivateEmployeeFunction {
        @Test
        void shouldInactivateEmployeeFunction() {
            // Arrange
            Long id = 1L;
            EmployeeFunction function = EmployeeFunction.builder().id(id).active(true).build();
            when(employeeFunctionGateway.findById(id)).thenReturn(Optional.of(function));

            // Act
            boolean result = employeeFunctionUseCase.inactivateEmployeeFunction(id);

            // Assert
            assertTrue(result);
            assertFalse(function.getActive());
            verify(employeeFunctionGateway).save(function);
        }
    }

    @Nested
    class ActivateEmployeeFunction {
        @Test
        void shouldActivateEmployeeFunction() {
            // Arrange
            Long id = 1L;
            EmployeeFunction function = EmployeeFunction.builder().id(id).active(false).build();
            when(employeeFunctionGateway.findById(id)).thenReturn(Optional.of(function));

            // Act
            boolean result = employeeFunctionUseCase.activateEmployeeFunction(id);

            // Assert
            assertTrue(result);
            assertTrue(function.getActive());
            verify(employeeFunctionGateway).save(function);
        }
    }
}
