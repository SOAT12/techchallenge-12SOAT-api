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

    public static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado.";
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public List<CustomerResponseDTO> getAllActiveCustomers() {
        return customerRepository.findAllByDeletedFalse().stream()
                .map(customerMapper::toCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toCustomerResponseDTO)
                .collect(Collectors.toList());
    }

    public CustomerResponseDTO getCustomerByCpf(String cpf) {
        return customerRepository.findByCpf(cpf)
                .map(customerMapper::toCustomerResponseDTO)
                .orElseThrow(() -> new NotFoundException(CLIENTE_NAO_ENCONTRADO));
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {
        if (customerRepository.findByCpf(requestDTO.getCpf()).isPresent()) {
            throw new BusinessException("CPF já cadastrado.");
        }

        Customer customer = customerMapper.toCustomer(requestDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerResponseDTO(savedCustomer);
    }

    public CustomerResponseDTO updateCustomerById(Long id, CustomerRequestDTO requestDTO) {
        Customer existingCustomer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(CLIENTE_NAO_ENCONTRADO));

        existingCustomer.setCpf(requestDTO.getCpf());
        existingCustomer.setName(requestDTO.getName());
        existingCustomer.setPhone(requestDTO.getPhone());
        existingCustomer.setEmail(requestDTO.getEmail());
        existingCustomer.setCity(requestDTO.getCity());
        existingCustomer.setState(requestDTO.getState());
        existingCustomer.setDistrict(requestDTO.getDistrict());
        existingCustomer.setStreet(requestDTO.getStreet());
        existingCustomer.setNumber(requestDTO.getNumber());

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return customerMapper.toCustomerResponseDTO(updatedCustomer);
    }

    public void deleteCustomerById(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(CLIENTE_NAO_ENCONTRADO));
        customer.setDeleted(true);
        customerRepository.save(customer);
    }

    public void activateCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CLIENTE_NAO_ENCONTRADO));
        customer.setDeleted(false);
        customerRepository.save(customer);
    }

}
