package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeFunctionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeFunctionJpaRepository extends JpaRepository<EmployeeFunctionJpaEntity, Long> {
}
