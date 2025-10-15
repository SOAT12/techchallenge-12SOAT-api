package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmployeeMapperTest {

    private EmployeeMapper employeeMapper;

    @BeforeEach
    void setUp() {
        employeeMapper = new EmployeeMapper();
    }

    @Nested
    class ToEmployee {
        @Test
        void shouldConvertToEmployee() {
            // Arrange
            EmployeeFunctionJpaEntity functionEntity = new EmployeeFunctionJpaEntity(1L, "Test", true);
            EmployeeJpaEntity entity = new EmployeeJpaEntity();
            entity.setId(1L);
            entity.setEmployeeFunction(functionEntity);

            // Act
            Employee result = employeeMapper.toEmployee(entity);

            // Assert
            assertNotNull(result);
            assertEquals(entity.getId(), result.getId());
        }
    }

    @Nested
    class ToEmployeeJpaEntity {
        @Test
        void shouldConvertToEmployeeJpaEntity() {
            // Arrange
            EmployeeFunction function = new EmployeeFunction(1L, "Test", true);
            Employee employee = Employee.builder()
                .id(1L)
                .employeeFunction(function)
                .build();

            // Act
            EmployeeJpaEntity result = employeeMapper.toEmployeeJpaEntity(employee);

            // Assert
            assertNotNull(result);
            assertEquals(employee.getId(), result.getId());
        }
    }
}
