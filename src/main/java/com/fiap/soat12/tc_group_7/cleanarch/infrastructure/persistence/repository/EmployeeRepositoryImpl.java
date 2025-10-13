package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.EmployeeJpaRepository;
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
    public List<EmployeeJpaEntity> findByEmployeeFunction(String function) {
        return employeeJpaRepository.findAllByEmployeeFunction_descriptionAndActiveTrue(function);
    }

    @Override
    public EmployeeJpaEntity save(EmployeeJpaEntity employee) {
        return employeeJpaRepository.save(employee);
    }
}
