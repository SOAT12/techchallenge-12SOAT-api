package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import org.springframework.stereotype.Component;

@Component
public class EmployeeFunctionMapper {
    public EmployeeFunctionResponseDTO toResponseDTO(EmployeeFunction entity) {
        if (entity == null) return null;
        EmployeeFunctionResponseDTO dto = new EmployeeFunctionResponseDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        return dto;
    }

    public EmployeeFunction toEntity(EmployeeFunctionRequestDTO dto) {
        if (dto == null) return null;
        EmployeeFunction entity = new EmployeeFunction();
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
