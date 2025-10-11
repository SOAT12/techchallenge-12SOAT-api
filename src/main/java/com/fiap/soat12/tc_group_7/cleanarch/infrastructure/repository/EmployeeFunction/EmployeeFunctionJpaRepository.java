package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeFunctionJpaRepository extends JpaRepository<EmployeeFunctionJpaEntity, Long> {
}
