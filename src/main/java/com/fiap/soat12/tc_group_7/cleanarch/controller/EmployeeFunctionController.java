package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.gateway.CustomerGateway;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EmployeeRepository;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.EmployeeFunctionPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.EmployeePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.CustomerUseCase;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class EmployeeFunctionController {

    private final EmployeeFunctionRepository employeeFunctionRepository;
    private final EmployeeFunctionPresenter employeeFunctionPresenter;

    public EmployeeFunctionController(EmployeeFunctionRepository employeeFunctionRepository) {
        this.employeeFunctionRepository = employeeFunctionRepository;
        this.employeeFunctionPresenter = new EmployeeFunctionPresenter();
    }

//    public List<CustomerResponseDTO> getAllActiveCustomers() {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        var customers = customerUseCase.getAllActiveCustomers();
//        return customers.stream()
//                .map(employeePresenter::toEmployeeResponseDTO)
//                .toList();
//    }
//
//    public List<CustomerResponseDTO> getAllCustomers() {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        var customers = customerUseCase.getAllCustomers();
//        return customers.stream()
//                .map(customerPresenter::toCustomerResponseDTO)
//                .toList();
//    }
//
//    public CustomerResponseDTO getCustomerByCpf(@RequestParam String cpf) {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        var customer = customerUseCase.getCustomerByCpf(cpf);
//        return customerPresenter.toCustomerResponseDTO(customer);
//    }
//
//    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        var customer = customerUseCase.createCustomer(requestDTO);
//        return customerPresenter.toCustomerResponseDTO(customer);
//    }
//
//    public CustomerResponseDTO updateCustomerById(Long id, CustomerRequestDTO requestDTO) {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        var customer = customerUseCase.updateCustomerById(id, requestDTO);
//        return customerPresenter.toCustomerResponseDTO(customer);
//    }
//
//    public void deleteCustomerById(Long id) {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        customerUseCase.deleteCustomerById(id);
//    }
//
//    public void activateCustomer(Long id) {
//        CustomerGateway customerGateway = new CustomerGateway(employeeFunctionRepository);
//        CustomerUseCase customerUseCase = new CustomerUseCase(customerGateway);
//        customerUseCase.activateCustomer(id);
//    }

}
