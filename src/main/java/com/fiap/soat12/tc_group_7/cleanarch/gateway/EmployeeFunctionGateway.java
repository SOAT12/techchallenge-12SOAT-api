package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.employee.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EmployeeFunctionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeFunctionGateway {

    private final EmployeeFunctionRepository employeeFunctionRepository;

    public List<EmployeeFunction> findAll() {
        return employeeFunctionRepository.findAll().stream()
                .map(this::toEmployeeFunction)
                .toList();
    }

    public Optional<EmployeeFunction> findById(Long id) {
        return employeeFunctionRepository.findById(id)
                .map(this::toEmployeeFunction);
    }

    public EmployeeFunction save(EmployeeFunction employeeFunction) {
        var employeeFunctionJpaEntity = employeeFunctionRepository.save(this.toEmployeeFunctionJpaEntity(employeeFunction));
        return this.toEmployeeFunction(employeeFunctionJpaEntity);
    }

    public void update(EmployeeFunction employeeFunction) {
        employeeFunctionRepository.save(this.toEmployeeFunctionJpaEntity(employeeFunction));
    }

    private EmployeeFunctionJpaEntity toEmployeeFunctionJpaEntity(EmployeeFunction employeeFunction) {
        return EmployeeFunctionJpaEntity.builder()
                .id(employeeFunction.getId())
                .description(employeeFunction.getDescription())
                .active(employeeFunction.getActive())
                .build();
    }

    private EmployeeFunction toEmployeeFunction(EmployeeFunctionJpaEntity employeeFunctionJpaEntity) {
        return EmployeeFunction.builder()
                .id(employeeFunctionJpaEntity.getId())
                .description(employeeFunctionJpaEntity.getDescription())
                .active(employeeFunctionJpaEntity.getActive())
                .build();
    }

}
