package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.CustomerRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomerGateway {

    private final CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll().stream()
                .map(this::toCustomer)
                .toList();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id)
                .map(this::toCustomer);
    }

    public Optional<Customer> findByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .map(this::toCustomer);
    }

    public Customer save(Customer customer) {
        var customerJpaEntity = customerRepository.save(this.toCustomerJpaEntity(customer));
        return this.toCustomer(customerJpaEntity);
    }

    public void update(Customer customer) {
        customerRepository.save(this.toCustomerJpaEntity(customer));
    }

    private CustomerJpaEntity toCustomerJpaEntity(Customer customer) {
        return CustomerJpaEntity.builder()
                .id(customer.getId())
                .cpf(customer.getCpf())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .city(customer.getCity())
                .state(customer.getState())
                .district(customer.getDistrict())
                .street(customer.getStreet())
                .number(customer.getNumber())
                .deleted(customer.getDeleted())
                .build();
    }

    private Customer toCustomer(CustomerJpaEntity customerJpaEntity) {
        return Customer.builder()
                .id(customerJpaEntity.getId())
                .cpf(customerJpaEntity.getCpf())
                .name(customerJpaEntity.getName())
                .phone(customerJpaEntity.getPhone())
                .email(customerJpaEntity.getEmail())
                .city(customerJpaEntity.getCity())
                .state(customerJpaEntity.getState())
                .district(customerJpaEntity.getDistrict())
                .street(customerJpaEntity.getStreet())
                .number(customerJpaEntity.getNumber())
                .deleted(customerJpaEntity.getDeleted())
                .createdAt(customerJpaEntity.getCreatedAt())
                .updatedAt(customerJpaEntity.getUpdatedAt())
                .build();
    }

}
