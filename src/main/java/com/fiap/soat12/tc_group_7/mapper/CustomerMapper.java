package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

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
                .build();
    }

    public Customer toCustomer(CustomerRequestDTO requestDTO) {
        return Customer.builder()
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .district(requestDTO.getDistrict())
                .street(requestDTO.getStreet())
                .number(requestDTO.getNumber())
                .deleted(false)
                .build();
    }

}
