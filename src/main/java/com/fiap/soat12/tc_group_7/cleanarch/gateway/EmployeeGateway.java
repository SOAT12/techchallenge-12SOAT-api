package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeGateway {

    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll() {
        return employeeRepository.findAll().stream()
                .map(this::toEmployee)
                .toList();
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id)
                .map(this::toEmployee);
    }

    public Optional<Employee> findByCpf(String cpf) {
        return employeeRepository.findByCpf(cpf)
                .map(this::toEmployee);
    }

    public List<Employee> findAllByEmployeeFunction(String function) {
        return employeeRepository.findByEmployeeFunction(function).stream()
                .map(this::toEmployee)
                .toList();
    }

    public Employee save(Employee employee) {
        var employeeJpaEntity = employeeRepository.save(this.toEmployeeJpaEntity(employee));
        return this.toEmployee(employeeJpaEntity);
    }

    public void update(Employee employee) {
        employeeRepository.save(this.toEmployeeJpaEntity(employee));
    }

    private EmployeeJpaEntity toEmployeeJpaEntity(Employee employee) {
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

    private Employee toEmployee(EmployeeJpaEntity employeeJpaEntity) {
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

}
