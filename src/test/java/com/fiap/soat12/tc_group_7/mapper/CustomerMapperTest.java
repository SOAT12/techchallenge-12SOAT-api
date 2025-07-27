package com.fiap.soat12.tc_group_7.mapper;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerMapperTest {

    private final CustomerMapper mapper = new CustomerMapper();

    @Test
    @DisplayName("Deve mapear Customer para CustomerResponseDTO corretamente")
    void toCustomerResponseDTO_DeveMapearCorretamente() {
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
        assertEquals(1L, dto.getId());
        assertEquals("123.456.789-00", dto.getCpf());
        assertEquals("João da Silva", dto.getName());
        assertEquals("99999-9999", dto.getPhone());
        assertEquals("joao@email.com", dto.getEmail());
        assertEquals("São Paulo", dto.getCity());
        assertEquals("SP", dto.getState());
        assertEquals("Centro", dto.getDistrict());
        assertEquals("Rua das Flores", dto.getStreet());
        assertEquals("100", dto.getNumber());
    }

}
