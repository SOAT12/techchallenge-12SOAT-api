package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    List<EmployeeJpaEntity> findAll();

    Optional<EmployeeJpaEntity> findById(Long id);

    Optional<EmployeeJpaEntity> findByCpf(String cpf);

    List<EmployeeJpaEntity> findByEmployeeFunction(String function);

    EmployeeJpaEntity save(EmployeeJpaEntity employee);

}
