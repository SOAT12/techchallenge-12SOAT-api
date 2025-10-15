package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.EmployeeFunctionJpaRepository;
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

class EmployeeFunctionRepositoryImplTest {

    @Mock
    private EmployeeFunctionJpaRepository employeeFunctionJpaRepository;

    @InjectMocks
    private EmployeeFunctionRepositoryImpl employeeFunctionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class FindAll {
        @Test
        void shouldFindAll() {
            // Arrange
            when(employeeFunctionJpaRepository.findAll()).thenReturn(List.of(new EmployeeFunctionJpaEntity()));

            // Act
            List<EmployeeFunctionJpaEntity> result = employeeFunctionRepository.findAll();

            // Assert
            assertFalse(result.isEmpty());
            verify(employeeFunctionJpaRepository).findAll();
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldFindById() {
            // Arrange
            Long id = 1L;
            when(employeeFunctionJpaRepository.findById(id)).thenReturn(Optional.of(new EmployeeFunctionJpaEntity()));

            // Act
            Optional<EmployeeFunctionJpaEntity> result = employeeFunctionRepository.findById(id);

            // Assert
            assertNotNull(result);
            verify(employeeFunctionJpaRepository).findById(id);
        }
    }

    @Nested
    class Save {
        @Test
        void shouldSave() {
            // Arrange
            EmployeeFunctionJpaEntity entity = new EmployeeFunctionJpaEntity();
            when(employeeFunctionJpaRepository.save(entity)).thenReturn(entity);

            // Act
            EmployeeFunctionJpaEntity result = employeeFunctionRepository.save(entity);

            // Assert
            assertNotNull(result);
            verify(employeeFunctionJpaRepository).save(entity);
        }
    }
}
