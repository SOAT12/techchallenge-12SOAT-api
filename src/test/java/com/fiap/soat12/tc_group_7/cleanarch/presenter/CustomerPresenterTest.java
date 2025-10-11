package com.fiap.soat12.tc_group_7.cleanarch.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Customer;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomerPresenterTest {

    private final CustomerPresenter presenter = new CustomerPresenter();

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
                .deleted(false)
                .build();

        // Act
        CustomerResponseDTO dto = presenter.toCustomerResponseDTO(customer);

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
        assertEquals(customer.getDeleted(), dto.getDeleted());
    }

}
