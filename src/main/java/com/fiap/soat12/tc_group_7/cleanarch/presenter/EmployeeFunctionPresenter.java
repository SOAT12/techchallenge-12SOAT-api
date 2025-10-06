package com.fiap.soat12.tc_group_7.cleanarch.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;

public class EmployeeFunctionPresenter {

    public EmployeeFunctionResponseDTO toEmployeeFunctionResponseDTO(EmployeeFunction employeeFunction) {
        if (employeeFunction == null) return null;
        return EmployeeFunctionResponseDTO.builder()
                .id(employeeFunction.getId())
                .description(employeeFunction.getDescription())
                .active(employeeFunction.getActive())
                .build();
    }

    public EmployeeFunction toEmployeeFunction(EmployeeFunctionRequestDTO dto) {
        if (dto == null) return null;
        return EmployeeFunction.builder()
                .description(dto.getDescription())
                .active(dto.getActive())
                .build();
    }
}
