package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerJpaEntity;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    List<CustomerJpaEntity> findAll();

    Optional<CustomerJpaEntity> findById(Long id);

    Optional<CustomerJpaEntity> findByCpf(String cpf);

    CustomerJpaEntity save(CustomerJpaEntity customer);

}
