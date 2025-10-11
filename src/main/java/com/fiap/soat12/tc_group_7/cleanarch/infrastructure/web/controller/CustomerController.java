package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.CustomerUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.CustomerPresenter;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class CustomerController {

    private final CustomerUseCase customerUseCase;
    private final CustomerPresenter customerPresenter;

    public CustomerController(CustomerUseCase customerUseCase, CustomerPresenter customerPresenter) {
        this.customerUseCase = customerUseCase;
        this.customerPresenter = customerPresenter;
    }

    public List<CustomerResponseDTO> getAllActiveCustomers() {
        var customers = customerUseCase.getAllActiveCustomers();
        return customers.stream()
                .map(customerPresenter::toCustomerResponseDTO)
                .toList();
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        var customers = customerUseCase.getAllCustomers();
        return customers.stream()
                .map(customerPresenter::toCustomerResponseDTO)
                .toList();
    }

    public CustomerResponseDTO getCustomerByCpf(@RequestParam String cpf) {
        var customer = customerUseCase.getCustomerByCpf(cpf);
        return customerPresenter.toCustomerResponseDTO(customer);
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        var customer = customerUseCase.createCustomer(requestDTO);
        return customerPresenter.toCustomerResponseDTO(customer);
    }

    public CustomerResponseDTO updateCustomerById(Long id, CustomerRequestDTO requestDTO) {
        var customer = customerUseCase.updateCustomerById(id, requestDTO);
        return customerPresenter.toCustomerResponseDTO(customer);
    }

    public void deleteCustomerById(Long id) {
        customerUseCase.deleteCustomerById(id);
    }

    public void activateCustomer(Long id) {
        customerUseCase.activateCustomer(id);
    }

}
