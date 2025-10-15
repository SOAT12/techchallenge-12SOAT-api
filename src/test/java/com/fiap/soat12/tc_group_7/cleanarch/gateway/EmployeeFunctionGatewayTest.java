package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
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

class EmployeeFunctionGatewayTest {

    @Mock
    private EmployeeFunctionRepository employeeFunctionRepository;

    @InjectMocks
    private EmployeeFunctionGateway employeeFunctionGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private EmployeeFunctionJpaEntity getEmployeeFunctionJpaEntity() {
        return new EmployeeFunctionJpaEntity(1L, "Test", true);
    }

    @Nested
    class FindAll {
        @Test
        void shouldFindAll() {
            // Arrange
            when(employeeFunctionRepository.findAll()).thenReturn(List.of(getEmployeeFunctionJpaEntity()));

            // Act
            List<EmployeeFunction> result = employeeFunctionGateway.findAll();

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeFunctionRepository).findAll();
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldFindById() {
            // Arrange
            Long id = 1L;
            when(employeeFunctionRepository.findById(id)).thenReturn(Optional.of(getEmployeeFunctionJpaEntity()));

            // Act
            Optional<EmployeeFunction> result = employeeFunctionGateway.findById(id);

            // Assert
            assertNotNull(result);
            verify(employeeFunctionRepository).findById(id);
        }
    }

    @Nested
    class Save {
        @Test
        void shouldSave() {
            // Arrange
            EmployeeFunction employeeFunction = EmployeeFunction.builder().build();
            when(employeeFunctionRepository.save(any())).thenReturn(getEmployeeFunctionJpaEntity());

            // Act
            EmployeeFunction result = employeeFunctionGateway.save(employeeFunction);

            // Assert
            assertNotNull(result);
            verify(employeeFunctionRepository).save(any());
        }
    }

    @Nested
    class Update {
        @Test
        void shouldUpdate() {
            // Arrange
            EmployeeFunction employeeFunction = EmployeeFunction.builder().build();

            // Act
            employeeFunctionGateway.update(employeeFunction);

            // Assert
            verify(employeeFunctionRepository).save(any());
        }
    }
}
