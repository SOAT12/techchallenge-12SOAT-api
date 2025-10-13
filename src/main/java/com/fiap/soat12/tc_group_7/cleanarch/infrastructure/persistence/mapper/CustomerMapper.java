package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;

public class CustomerMapper {

    public Customer toCustomer(CustomerJpaEntity customerJpaEntity) {
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

    public CustomerJpaEntity toCustomerJpaEntity(Customer customer) {
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

}
