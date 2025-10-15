package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.EmployeeJpaRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeRepositoryImplTest {

    @Mock
    private EmployeeJpaRepository employeeJpaRepository;

    @InjectMocks
    private EmployeeRepositoryImpl employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class FindAll {
        @Test
        void shouldFindAll() {
            // Arrange
            when(employeeJpaRepository.findAll()).thenReturn(List.of(new EmployeeJpaEntity()));

            // Act
            List<EmployeeJpaEntity> result = employeeRepository.findAll();

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeJpaRepository).findAll();
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldFindById() {
            // Arrange
            Long id = 1L;
            when(employeeJpaRepository.findById(id)).thenReturn(Optional.of(new EmployeeJpaEntity()));

            // Act
            Optional<EmployeeJpaEntity> result = employeeRepository.findById(id);

            // Assert
            assertNotNull(result);
            verify(employeeJpaRepository).findById(id);
        }
    }

    @Nested
    class FindByCpf {
        @Test
        void shouldFindByCpf() {
            // Arrange
            String cpf = "123";
            when(employeeJpaRepository.findByCpf(cpf)).thenReturn(Optional.of(new EmployeeJpaEntity()));

            // Act
            Optional<EmployeeJpaEntity> result = employeeRepository.findByCpf(cpf);

            // Assert
            assertNotNull(result);
            verify(employeeJpaRepository).findByCpf(cpf);
        }
    }

    @Nested
    class FindByEmployeeFunction {
        @Test
        void shouldFindByEmployeeFunction() {
            // Arrange
            String function = "test";
            when(employeeJpaRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(function)).thenReturn(List.of(new EmployeeJpaEntity()));

            // Act
            List<EmployeeJpaEntity> result = employeeRepository.findByEmployeeFunction(function);

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeJpaRepository).findAllByEmployeeFunction_descriptionAndActiveTrue(function);
        }
    }

    @Nested
    class Save {
        @Test
        void shouldSave() {
            // Arrange
            EmployeeJpaEntity entity = new EmployeeJpaEntity();
            when(employeeJpaRepository.save(entity)).thenReturn(entity);

            // Act
            EmployeeJpaEntity result = employeeRepository.save(entity);

            // Assert
            assertNotNull(result);
            verify(employeeJpaRepository).save(entity);
        }
    }
}
