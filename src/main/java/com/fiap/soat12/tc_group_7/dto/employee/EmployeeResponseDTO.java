package com.fiap.soat12.tc_group_7.dto.employee;

import lombok.Data;

@Data
public class EmployeeResponseDTO {
    private Long id;
    private String cpf;
    private String name;
    private String phone;
    private String email;
    private Boolean active;
    private String createdAt;
    private String updatedAt;
    private EmployeeFunctionResponseDTO employeeFunction;
}

