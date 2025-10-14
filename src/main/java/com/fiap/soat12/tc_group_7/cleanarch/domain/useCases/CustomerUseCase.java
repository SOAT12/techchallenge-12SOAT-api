package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.CustomerGateway;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CustomerUseCase {

    protected static final String CUSTOMER_NOT_FOUND_MESSAGE = "Cliente não encontrado.";
    protected static final String CUSTOMER_ALREADY_EXISTS_MESSAGE = "Já existe um cliente cadastrado com o documento informado.";

    private final CustomerGateway customerGateway;

    public List<Customer> getAllActiveCustomers() {
        return customerGateway.findAll().stream()
                .filter(customer -> !customer.getDeleted())
                .toList();
    }

    public List<Customer> getAllCustomers() {
        return customerGateway.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));
    }

    public Customer getCustomerByCpf(String cpf) {
        return customerGateway.findByCpf(cpf)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));
    }

    public Customer createCustomer(CustomerRequestDTO requestDTO) {
        if (customerGateway.findByCpf(requestDTO.getCpf()).isPresent()) {
            throw new IllegalArgumentException(CUSTOMER_ALREADY_EXISTS_MESSAGE);
        }

        Customer customer = Customer.builder()
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .district(requestDTO.getDistrict())
                .street(requestDTO.getStreet())
                .number(requestDTO.getNumber())
                .deleted(Boolean.FALSE)
                .build();
        return customerGateway.save(customer);
    }

    public Customer updateCustomerById(Long id, CustomerRequestDTO requestDTO) {
        var customer = customerGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));

        customer.setCpf(requestDTO.getCpf());
        customer.setName(requestDTO.getName());
        customer.setPhone(requestDTO.getPhone());
        customer.setEmail(requestDTO.getEmail());
        customer.setCity(requestDTO.getCity());
        customer.setState(requestDTO.getState());
        customer.setDistrict(requestDTO.getDistrict());
        customer.setStreet(requestDTO.getStreet());
        customer.setNumber(requestDTO.getNumber());

        customerGateway.update(customer);
        return customer;
    }

    public void deleteCustomerById(Long id) {
        var customer = customerGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));
        customer.setDeleted(Boolean.TRUE);
        customerGateway.update(customer);
    }

    public void activateCustomer(Long id) {
        var customer = customerGateway.findById(id)
                .orElseThrow(() -> new NotFoundException(CUSTOMER_NOT_FOUND_MESSAGE));
        customer.setDeleted(Boolean.FALSE);
        customerGateway.update(customer);
    }

}
