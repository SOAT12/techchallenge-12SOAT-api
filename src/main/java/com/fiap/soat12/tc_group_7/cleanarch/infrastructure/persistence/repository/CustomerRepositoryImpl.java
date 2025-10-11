package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.CustomerRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public List<CustomerJpaEntity> findAll() {
        return customerJpaRepository.findAll();
    }

    @Override
    public Optional<CustomerJpaEntity> findById(Long id) {
        return customerJpaRepository.findById(id);
    }

    @Override
    public Optional<CustomerJpaEntity> findByCpf(String cpf) {
        return customerJpaRepository.findByCpf(cpf);
    }

    @Override
    public CustomerJpaEntity save(CustomerJpaEntity customer) {
        return customerJpaRepository.save(customer);
    }

}
