package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {

    Optional<CustomerJpaEntity> findByCpf(String cpf);

}
