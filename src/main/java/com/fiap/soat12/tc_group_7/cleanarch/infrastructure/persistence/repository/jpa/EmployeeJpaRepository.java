package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeJpaEntity, Long> {

    List<EmployeeJpaEntity> findAllByActiveTrue();

    List<EmployeeJpaEntity> findAllByEmployeeFunction_descriptionAndActiveTrue(String employeeFunctionDescription);

    Optional<EmployeeJpaEntity> findByCpf(String cpf);
}
