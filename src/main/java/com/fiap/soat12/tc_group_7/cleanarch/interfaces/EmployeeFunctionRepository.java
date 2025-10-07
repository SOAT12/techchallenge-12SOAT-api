package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction.EmployeeFunctionJpaEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeFunctionRepository {

    List<EmployeeFunctionJpaEntity> findAll();

    Optional<EmployeeFunctionJpaEntity> findById(Long id);

    EmployeeFunctionJpaEntity save(EmployeeFunctionJpaEntity employeeFunction);
}
