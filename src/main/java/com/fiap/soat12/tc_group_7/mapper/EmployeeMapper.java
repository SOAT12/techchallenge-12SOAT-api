package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    @Autowired
    private EmployeeFunctionMapper employeeFunctionMapper;

    public EmployeeResponseDTO toResponseDTO(Employee entity) {
        if (entity == null) return null;
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setId(entity.getId());
        dto.setCpf(entity.getCpf());
        dto.setName(entity.getName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setActive(entity.getActive());
        dto.setCreatedAt(entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null);
        dto.setUpdatedAt(entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null);
        dto.setEmployeeFunction(employeeFunctionMapper.toResponseDTO(entity.getEmployeeFunction()));
        return dto;
    }

    public Employee toEntity(EmployeeRequestDTO dto, EmployeeFunction employeeFunction) {
        if (dto == null) return null;
        return Employee.builder()
                .cpf(dto.getCpf())
                .name(dto.getName())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .active(dto.getActive())
                .employeeFunction(employeeFunction)
                .build();
    }
}

