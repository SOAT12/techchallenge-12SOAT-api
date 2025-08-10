package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeFunctionMapperTest {

    private final EmployeeFunctionMapper mapper = new EmployeeFunctionMapper();

    @Test
    @DisplayName("Deve mapear EmployeeFunction para EmployeeFunctionResponseDTO corretamente")
    void toEmployeeFunctionResponseDTO_DeveMapearCorretamente() {
        EmployeeFunction employeeFunction = EmployeeFunction.builder()
                .id(1L)
                .description("Gerente")
                .build();

        EmployeeFunctionResponseDTO dto = mapper.toEmployeeFunctionResponseDTO(employeeFunction);

        assertEquals(1L, dto.getId());
        assertEquals("Gerente", dto.getDescription());
    }

    @Test
    @DisplayName("Deve mapear EmployeeFunctionRequestDTO para EmployeeFunction corretamente")
    void toEmployeeFunction_DeveMapearCorretamente() {
        EmployeeFunctionRequestDTO requestDTO = EmployeeFunctionRequestDTO.builder()
                .description("Analista")
                .build();

        EmployeeFunction entity = mapper.toEmployeeFunction(requestDTO);

        assertEquals("Analista", entity.getDescription());
    }
}

