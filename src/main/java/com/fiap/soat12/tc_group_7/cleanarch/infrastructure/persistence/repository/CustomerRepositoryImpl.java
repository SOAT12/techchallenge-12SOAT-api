package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.CustomerRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;
    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream()
                .map(customerMapper::toCustomer)
                .toList();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id)
                .map(customerMapper::toCustomer);
    }

    @Override
    public Optional<Customer> findByCpf(String cpf) {
        return customerJpaRepository.findByCpf(cpf)
                .map(customerMapper::toCustomer);
    }

    @Override
    public Customer save(Customer customer) {
        var savedCustomer = customerJpaRepository.save(customerMapper.toCustomerJpaEntity(customer));
        return customerMapper.toCustomer(savedCustomer);
    }

}
