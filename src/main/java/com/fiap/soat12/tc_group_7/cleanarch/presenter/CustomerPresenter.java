package com.fiap.soat12.tc_group_7.cleanarch.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Customer;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;

public class CustomerPresenter {

    public CustomerResponseDTO toCustomerResponseDTO(Customer customer) {
        return CustomerResponseDTO.builder()
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
