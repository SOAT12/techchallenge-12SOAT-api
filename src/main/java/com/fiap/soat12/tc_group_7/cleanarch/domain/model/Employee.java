package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Employee {

    private Long id;

    private Date createdAt;

    private Date updatedAt;

    private EmployeeFunction employeeFunction;

    private String cpf;

    private String name;

    private String password;

    private String phone;

    private String email;

    private Boolean active;

    private String temporaryPassword;

    private LocalDateTime passwordValidity;

    private Boolean useTemporaryPassword;

}
