package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;

public class EmployeeMapper {

    public Employee toEmployee(EmployeeJpaEntity employeeJpaEntity) {
        return Employee.builder()
                .id(employeeJpaEntity.getId())
                .createdAt(employeeJpaEntity.getCreatedAt())
                .updatedAt(employeeJpaEntity.getUpdatedAt())
                .employeeFunction(
                        new EmployeeFunction(
                                employeeJpaEntity.getEmployeeFunction().getId(),
                                employeeJpaEntity.getEmployeeFunction().getDescription(),
                                employeeJpaEntity.getEmployeeFunction().getActive()
                        )
                )
                .cpf(employeeJpaEntity.getCpf())
                .name(employeeJpaEntity.getName())
                .password(employeeJpaEntity.getPassword())
                .phone(employeeJpaEntity.getPhone())
                .email(employeeJpaEntity.getEmail())
                .active(employeeJpaEntity.getActive())
                .temporaryPassword(employeeJpaEntity.getTemporaryPassword())
                .passwordValidity(employeeJpaEntity.getPasswordValidity())
                .useTemporaryPassword(employeeJpaEntity.getUseTemporaryPassword())
                .build();
    }

    public EmployeeJpaEntity toEmployeeJpaEntity(Employee employee) {
        return EmployeeJpaEntity.builder()
                .id(employee.getId())
                .createdAt(employee.getCreatedAt())
                .updatedAt(employee.getUpdatedAt())
                .employeeFunction(
                        new EmployeeFunctionJpaEntity(
                                employee.getEmployeeFunction().getId(),
                                employee.getEmployeeFunction().getDescription(),
                                employee.getEmployeeFunction().getActive()
                        )
                )
                .cpf(employee.getCpf())
                .name(employee.getName())
                .password(employee.getPassword())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .active(employee.getActive())
                .temporaryPassword(employee.getTemporaryPassword())
                .passwordValidity(employee.getPasswordValidity())
                .useTemporaryPassword(employee.getUseTemporaryPassword())
                .build();
    }

}
