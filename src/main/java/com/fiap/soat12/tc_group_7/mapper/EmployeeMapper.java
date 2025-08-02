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

    public EmployeeResponseDTO toEmployeeResponseDTO(Employee employee) {
        if (employee == null) return null;
        return EmployeeResponseDTO.builder()
                .id(employee.getId())
                .cpf(employee.getCpf())
                .name(employee.getName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .active(employee.getActive())
                .created_at(employee.getCreatedAt())
                .updated_at(employee.getUpdatedAt())
                .employeeFunction(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employee.getEmployeeFunction()))
                .build();
    }

    public Employee toEmployee(EmployeeRequestDTO dto, EmployeeFunction employeeFunction) {
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
