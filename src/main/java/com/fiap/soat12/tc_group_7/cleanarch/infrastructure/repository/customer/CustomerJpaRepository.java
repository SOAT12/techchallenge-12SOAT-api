package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerJpaRepository extends JpaRepository<CustomerJpaEntity, Long> {

    Optional<CustomerJpaEntity> findByCpf(String cpf);

}
