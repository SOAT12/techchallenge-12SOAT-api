package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EmployeePresenterTest {

    @Mock
    private EmployeeFunctionPresenter employeeFunctionPresenter;

    @InjectMocks
    private EmployeePresenter employeePresenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class ToEmployeeResponseDTO {
        @Test
        void shouldConvertToEmployeeResponseDTO() {
            // Arrange
            Employee employee = Employee.builder()
                .id(1L)
                .employeeFunction(EmployeeFunction.builder().build())
                .build();
            when(employeeFunctionPresenter.toEmployeeFunctionResponseDTO(any())).thenReturn(new com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO());

            // Act
            EmployeeResponseDTO result = employeePresenter.toEmployeeResponseDTO(employee);

            // Assert
            assertNotNull(result);
            assertEquals(employee.getId(), result.getId());
        }
    }

    @Nested
    class ToEmployee {
        @Test
        void shouldConvertToEmployee() {
            // Arrange
            EmployeeRequestDTO dto = new EmployeeRequestDTO();
            dto.setPassword("password");
            EmployeeFunction function = EmployeeFunction.builder().build();

            // Act
            Employee result = employeePresenter.toEmployee(dto, function);

            // Assert
            assertNotNull(result);
        }
    }
}
