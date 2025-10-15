package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeFunctionGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.EmployeePresenter;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeUseCaseTest {

    @Mock
    private EmployeeGateway employeeGateway;

    @Mock
    private EmployeeFunctionGateway employeeFunctionGateway;

    @Mock
    private EmployeePresenter employeePresenter;

    @InjectMocks
    private EmployeeUseCase employeeUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class GetAllEmployees {
        @Test
        void shouldReturnAllEmployees() {
            // Arrange
            Employee employee1 = Employee.builder().build();
            Employee employee2 = Employee.builder().build();
            when(employeeGateway.findAll()).thenReturn(List.of(employee1, employee2));

            // Act
            List<Employee> result = employeeUseCase.getAllEmployees();

            // Assert
            assertEquals(2, result.size());
            verify(employeeGateway, times(1)).findAll();
        }
    }

    @Nested
    class GetAllActiveEmployees {
        @Test
        void shouldReturnOnlyActiveEmployees() {
            // Arrange
            Employee activeEmployee = Employee.builder().active(true).build();
            activeEmployee.setActive(true);
            Employee inactiveEmployee = Employee.builder().active(false).build();
            inactiveEmployee.setActive(false);
            when(employeeGateway.findAll()).thenReturn(List.of(activeEmployee, inactiveEmployee));

            // Act
            List<Employee> result = employeeUseCase.getAllActiveEmployees();

            // Assert
            assertEquals(1, result.size());
            assertEquals(activeEmployee, result.get(0));
            verify(employeeGateway, times(1)).findAll();
        }
    }

    @Nested
    class GetEmployeeById {
        @Test
        void shouldReturnEmployeeWhenFound() {
            // Arrange
            Long id = 1L;
            Employee employee = Employee.builder().build();
            when(employeeGateway.findById(id)).thenReturn(Optional.of(employee));

            // Act
            Employee result = employeeUseCase.getEmployeeById(id);

            // Assert
            assertNotNull(result);
            verify(employeeGateway, times(1)).findById(id);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenEmployeeNotFound() {
            // Arrange
            Long id = 1L;
            when(employeeGateway.findById(id)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> employeeUseCase.getEmployeeById(id));
            verify(employeeGateway, times(1)).findById(id);
        }
    }

    @Nested
    class GetByEmployeeFunction {
        @Test
        void shouldReturnEmployeesForAGivenFunction() {
            // Arrange
            String function = "Manager";
            Employee employee1 = Employee.builder().build();
            Employee employee2 = Employee.builder().build();
            when(employeeGateway.findAllByEmployeeFunction(function)).thenReturn(List.of(employee1, employee2));

            // Act
            List<Employee> result = employeeUseCase.getByEmployeeFunction(function);

            // Assert
            assertEquals(2, result.size());
            verify(employeeGateway, times(1)).findAllByEmployeeFunction(function);
        }
    }

    @Nested
    class CreateEmployee {
        @Test
        void shouldCreateEmployee() {
            // Arrange
            EmployeeRequestDTO dto = new EmployeeRequestDTO();
            dto.setEmployeeFunctionId(1L);
            EmployeeFunction function = EmployeeFunction.builder().build();
            Employee employee = Employee.builder().build();

            when(employeeFunctionGateway.findById(1L)).thenReturn(Optional.of(function));
            when(employeePresenter.toEmployee(dto, function)).thenReturn(employee);
            when(employeeGateway.save(employee)).thenReturn(employee);

            // Act
            Employee result = employeeUseCase.createEmployee(dto);

            // Assert
            assertNotNull(result);
            assertTrue(result.getActive());
            assertFalse(result.getUseTemporaryPassword());
            verify(employeeGateway, times(1)).save(employee);
        }

        @Test
        void shouldThrowExceptionWhenFunctionNotFound() {
            // Arrange
            EmployeeRequestDTO dto = new EmployeeRequestDTO();
            dto.setEmployeeFunctionId(1L);

            when(employeeFunctionGateway.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> employeeUseCase.createEmployee(dto));
            verify(employeeGateway, never()).save(any());
        }
    }

    @Nested
    class UpdateEmployee {
        @Test
        void shouldUpdateEmployee() {
            // Arrange
            Long id = 1L;
            EmployeeRequestDTO dto = new EmployeeRequestDTO();
            dto.setEmployeeFunctionId(1L);
            dto.setName("New Name");

            Employee existingEmployee = Employee.builder().build();
            EmployeeFunction function = EmployeeFunction.builder().build();

            when(employeeGateway.findById(id)).thenReturn(Optional.of(existingEmployee));
            when(employeeFunctionGateway.findById(1L)).thenReturn(Optional.of(function));
            when(employeeGateway.save(existingEmployee)).thenReturn(existingEmployee);

            // Act
            Optional<Employee> result = employeeUseCase.updateEmployee(id, dto);

            // Assert
            assertTrue(result.isPresent());
            assertEquals("New Name", result.get().getName());
            verify(employeeGateway, times(1)).save(existingEmployee);
        }

        @Test
        void shouldReturnEmptyWhenEmployeeNotFound() {
            // Arrange
            Long id = 1L;
            EmployeeRequestDTO dto = new EmployeeRequestDTO();

            when(employeeGateway.findById(id)).thenReturn(Optional.empty());

            // Act
            Optional<Employee> result = employeeUseCase.updateEmployee(id, dto);

            // Assert
            assertFalse(result.isPresent());
            verify(employeeGateway, never()).save(any());
        }
    }

    @Nested
    class InactivateEmployee {
        @Test
        void shouldInactivateEmployee() {
            // Arrange
            Long id = 1L;
            Employee employee = Employee.builder().active(true).build();

            when(employeeGateway.findById(id)).thenReturn(Optional.of(employee));

            // Act
            boolean result = employeeUseCase.inactivateEmployee(id);

            // Assert
            assertTrue(result);
            assertFalse(employee.getActive());
            verify(employeeGateway, times(1)).save(employee);
        }

        @Test
        void shouldReturnFalseWhenInactivatingAndEmployeeNotFound() {
            // Arrange
            Long id = 1L;

            when(employeeGateway.findById(id)).thenReturn(Optional.empty());

            // Act
            boolean result = employeeUseCase.inactivateEmployee(id);

            // Assert
            assertFalse(result);
            verify(employeeGateway, never()).save(any());
        }
    }

    @Nested
    class ActivateEmployee {
        @Test
        void shouldActivateEmployee() {
            // Arrange
            Long id = 1L;
            Employee employee = Employee.builder().active(false).build();

            when(employeeGateway.findById(id)).thenReturn(Optional.of(employee));

            // Act
            boolean result = employeeUseCase.activateEmployee(id);

            // Assert
            assertTrue(result);
            assertTrue(employee.getActive());
            verify(employeeGateway, times(1)).save(employee);
        }

        @Test
        void shouldReturnFalseWhenActivatingAndEmployeeNotFound() {
            // Arrange
            Long id = 1L;

            when(employeeGateway.findById(id)).thenReturn(Optional.empty());

            // Act
            boolean result = employeeUseCase.activateEmployee(id);

            // Assert
            assertFalse(result);
            verify(employeeGateway, never()).save(any());
        }
    }
}
