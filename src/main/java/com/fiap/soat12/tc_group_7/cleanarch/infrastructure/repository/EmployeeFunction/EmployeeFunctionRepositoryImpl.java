package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.EmployeeFunctionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeFunctionRepositoryImpl implements EmployeeFunctionRepository {

    private final EmployeeFunctionJpaRepository employeeFunctionJpaRepository;

    @Override
    public List<EmployeeFunctionJpaEntity> findAll() {
        return employeeFunctionJpaRepository.findAll();
    }

    @Override
    public Optional<EmployeeFunctionJpaEntity> findById(Long id) {
        return employeeFunctionJpaRepository.findById(id);
    }

    @Override
    public EmployeeFunctionJpaEntity save(EmployeeFunctionJpaEntity employee) {
        return employeeFunctionJpaRepository.save(employee);
    }
}
