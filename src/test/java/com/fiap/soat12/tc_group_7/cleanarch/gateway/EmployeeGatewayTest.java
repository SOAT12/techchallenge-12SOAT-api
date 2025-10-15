package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
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

class EmployeeGatewayTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeGateway employeeGateway;

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
    class FindAll {
        @Test
        void shouldFindAll() {
            // Arrange
            when(employeeRepository.findAll()).thenReturn(List.of(getEmployeeJpaEntity()));

            // Act
            List<Employee> result = employeeGateway.findAll();

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeRepository).findAll();
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldFindById() {
            // Arrange
            Long id = 1L;
            when(employeeRepository.findById(id)).thenReturn(Optional.of(getEmployeeJpaEntity()));

            // Act
            Optional<Employee> result = employeeGateway.findById(id);

            // Assert
            assertNotNull(result);
            verify(employeeRepository).findById(id);
        }
    }

    @Nested
    class FindByCpf {
        @Test
        void shouldFindByCpf() {
            // Arrange
            String cpf = "123";
            when(employeeRepository.findByCpf(cpf)).thenReturn(Optional.of(getEmployeeJpaEntity()));

            // Act
            Optional<Employee> result = employeeGateway.findByCpf(cpf);

            // Assert
            assertNotNull(result);
            verify(employeeRepository).findByCpf(cpf);
        }
    }

    @Nested
    class FindAllByEmployeeFunction {
        @Test
        void shouldFindAllByEmployeeFunction() {
            // Arrange
            String function = "test";
            when(employeeRepository.findByEmployeeFunction(function)).thenReturn(List.of(getEmployeeJpaEntity()));

            // Act
            List<Employee> result = employeeGateway.findAllByEmployeeFunction(function);

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeRepository).findByEmployeeFunction(function);
        }
    }

    @Nested
    class Save {
        @Test
        void shouldSave() {
            // Arrange
            EmployeeFunction function = new EmployeeFunction(1L, "Test", true);
            Employee employee = Employee.builder()
                .id(1L)
                .employeeFunction(function)
                .build();
            when(employeeRepository.save(any())).thenReturn(getEmployeeJpaEntity());

            // Act
            Employee result = employeeGateway.save(employee);

            // Assert
            assertNotNull(result);
            verify(employeeRepository).save(any());
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdate() {
            // Arrange
            EmployeeFunction function = new EmployeeFunction(1L, "Test", true);
            Employee employee = Employee.builder()
                .id(1L)
                .employeeFunction(function)
                .build();

            // Act
            employeeGateway.update(employee);

            // Assert
            verify(employeeRepository).save(any());
        }
    }
}
