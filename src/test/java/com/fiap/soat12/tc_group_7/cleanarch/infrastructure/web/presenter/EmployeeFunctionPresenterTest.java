package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmployeeFunctionPresenterTest {

    private EmployeeFunctionPresenter employeeFunctionPresenter;

    @BeforeEach
    void setUp() {
        employeeFunctionPresenter = new EmployeeFunctionPresenter();
    }

    @Nested
    class ToEmployeeFunctionResponseDTO {
        @Test
        void shouldConvertToEmployeeFunctionResponseDTO() {
            // Arrange
            EmployeeFunction employeeFunction = EmployeeFunction.builder()
                .id(1L)
                .description("Test")
                .active(true)
                .build();

            // Act
            EmployeeFunctionResponseDTO result = employeeFunctionPresenter.toEmployeeFunctionResponseDTO(employeeFunction);

            // Assert
            assertNotNull(result);
            assertEquals(employeeFunction.getId(), result.getId());
        }
    }

    @Nested
    class ToEmployeeFunction {
        @Test
        void shouldConvertToEmployeeFunction() {
            // Arrange
            EmployeeFunctionRequestDTO dto = new EmployeeFunctionRequestDTO();
            dto.setDescription("Test");

            // Act
            EmployeeFunction result = employeeFunctionPresenter.toEmployeeFunction(dto);

            // Assert
            assertNotNull(result);
            assertEquals(dto.getDescription(), result.getDescription());
        }
    }
}
