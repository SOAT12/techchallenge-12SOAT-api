package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.employee;

import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final EmployeeJpaRepository employeeJpaRepository;

    @Override
    public List<EmployeeJpaEntity> findAll() {
        return employeeJpaRepository.findAll();
    }

    @Override
    public Optional<EmployeeJpaEntity> findById(Long id) {
        return employeeJpaRepository.findById(id);
    }

    @Override
    public Optional<EmployeeJpaEntity> findByCpf(String cpf) {
        return employeeJpaRepository.findByCpf(cpf);
    }

    @Override
    public EmployeeJpaEntity save(EmployeeJpaEntity employee) {
        return employeeJpaRepository.save(employee);
    }

    @Override
    public List<EmployeeJpaEntity> findAllByActiveTrue() { return employeeJpaRepository.findAllByActiveTrue(); }

    @Override
    public List<EmployeeJpaEntity> findAllByEmployeeFunction_descriptionAndActiveTrue(String employeeFunctionDescription) { return employeeJpaRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(employeeFunctionDescription); }
}
