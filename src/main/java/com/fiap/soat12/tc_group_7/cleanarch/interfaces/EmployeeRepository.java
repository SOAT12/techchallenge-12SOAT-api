package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.employee.EmployeeJpaEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    List<EmployeeJpaEntity> findAll();

    Optional<EmployeeJpaEntity> findById(Long id);

    Optional<EmployeeJpaEntity> findByCpf(String cpf);

    EmployeeJpaEntity save(EmployeeJpaEntity employee);

}
