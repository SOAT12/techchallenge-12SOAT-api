package com.fiap.soat12.tc_group_7.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private Long id;
    private String cpf;
    private String name;
    private String phone;
    private String email;
    private Boolean active;
    private Date created_at;
    private Date updated_at;
    private EmployeeFunctionResponseDTO employeeFunction;
}

