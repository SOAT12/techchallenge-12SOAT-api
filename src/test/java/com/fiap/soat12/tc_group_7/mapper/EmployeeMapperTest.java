package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeMapperTest {

    @Mock
    private EmployeeFunctionMapper employeeFunctionMapper;

    @InjectMocks
    private EmployeeMapper mapper;

    @Test
    @DisplayName("Deve mapear Employee para EmployeeResponseDTO corretamente")
    void toEmployeeResponseDTO_DeveMapearCorretamente() {
        // Arrange
        EmployeeFunction employeeFunction = EmployeeFunction.builder()
                .id(2L)
                .description("Gerente")
                .build();
        EmployeeFunctionResponseDTO functionDTO = EmployeeFunctionResponseDTO.builder()
                .id(2L)
                .description("Gerente")
                .build();
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employeeFunction)).thenReturn(functionDTO);

        Employee employee = Employee.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .employeeFunction(employeeFunction)
                .password("senha123")
                .build();

        // Act
        EmployeeResponseDTO dto = mapper.toEmployeeResponseDTO(employee);

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("123.456.789-00", dto.getCpf());
        assertEquals("Maria Souza", dto.getName());
        assertEquals("88888-8888", dto.getPhone());
        assertEquals("maria@email.com", dto.getEmail());
        assertEquals(true, dto.getActive());
        assertEquals(employee.getCreatedAt(), dto.getCreated_at());
        assertEquals(employee.getUpdatedAt(), dto.getUpdated_at());
        assertNotNull(dto.getEmployeeFunction());
        assertEquals(2L, dto.getEmployeeFunction().getId());
        assertEquals("Gerente", dto.getEmployeeFunction().getDescription());
    }
}

