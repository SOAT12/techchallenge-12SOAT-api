package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import com.fiap.soat12.tc_group_7.exception.BusinessException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO getCustomerByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .map(customerMapper::toCustomerResponseDTO)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        if (customerRepository.findByCpf(requestDTO.getCpf()).isPresent()) {
            throw new BusinessException("CPF já cadastrado.");
        }

        Customer customer = customerMapper.toCustomer(requestDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDTO(savedCustomer);
    }

}
