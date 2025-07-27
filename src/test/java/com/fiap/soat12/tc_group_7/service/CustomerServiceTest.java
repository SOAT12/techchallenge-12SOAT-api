package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import com.fiap.soat12.tc_group_7.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve retornar todos os clientes convertidos em DTO")
    void getAllCustomers_DeveRetornarListaDeDTOs() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .build();
        CustomerResponseDTO dto = CustomerResponseDTO.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toCustomerResponseDTO(customer)).thenReturn(dto);

        // Act
        List<CustomerResponseDTO> result = customerService.getAllCustomers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getName());
        assertEquals("123.456.789-00", result.get(0).getCpf());
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toCustomerResponseDTO(customer);
    }

}
