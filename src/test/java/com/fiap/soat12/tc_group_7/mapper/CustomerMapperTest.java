package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {

    private final CustomerMapper mapper = new CustomerMapper();

    @Test
    void toCustomerResponseDTO_withSuccess() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("João da Silva")
                .phone("99999-9999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua das Flores")
                .number("100")
                .build();

        // Act
        CustomerResponseDTO dto = mapper.toCustomerResponseDTO(customer);

        // Assert
        assertEquals(customer.getId(), dto.getId());
        assertEquals(customer.getCpf(), dto.getCpf());
        assertEquals(customer.getName(), dto.getName());
        assertEquals(customer.getPhone(), dto.getPhone());
        assertEquals(customer.getEmail(), dto.getEmail());
        assertEquals(customer.getCity(), dto.getCity());
        assertEquals(customer.getState(), dto.getState());
        assertEquals(customer.getDistrict(), dto.getDistrict());
        assertEquals(customer.getStreet(), dto.getStreet());
        assertEquals(customer.getNumber(), dto.getNumber());
    }

    @Test
    void toCustomer_withSuccess() {
        // Arrange
        CustomerRequestDTO dto = CustomerRequestDTO.builder()
                .cpf("12345678900")
                .name("João")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .build();

        // Act
        Customer customer = mapper.toCustomer(dto);

        // Assert
        assertEquals(dto.getCpf(), customer.getCpf());
        assertEquals(dto.getName(), customer.getName());
        assertEquals(dto.getPhone(), customer.getPhone());
        assertEquals(dto.getEmail(), customer.getEmail());
        assertEquals(dto.getCity(), customer.getCity());
        assertEquals(dto.getState(), customer.getState());
        assertEquals(dto.getDistrict(), customer.getDistrict());
        assertEquals(dto.getStreet(), customer.getStreet());
        assertEquals(dto.getNumber(), customer.getNumber());
    }

}
