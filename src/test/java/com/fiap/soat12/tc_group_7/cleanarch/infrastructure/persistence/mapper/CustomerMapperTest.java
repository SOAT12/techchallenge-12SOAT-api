package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setup() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void toCustomer_ShouldMapAllFields() {
        CustomerJpaEntity entity = CustomerJpaEntity.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("123")
                .deleted(false)
                .build();

        Customer customer = customerMapper.toCustomer(entity);

        assertNotNull(customer);
        assertEquals(entity.getId(), customer.getId());
        assertEquals(entity.getCpf(), customer.getCpf());
        assertEquals(entity.getName(), customer.getName());
        assertEquals(entity.getPhone(), customer.getPhone());
        assertEquals(entity.getEmail(), customer.getEmail());
        assertEquals(entity.getCity(), customer.getCity());
        assertEquals(entity.getState(), customer.getState());
        assertEquals(entity.getDistrict(), customer.getDistrict());
        assertEquals(entity.getStreet(), customer.getStreet());
        assertEquals(entity.getNumber(), customer.getNumber());
        assertEquals(entity.getDeleted(), customer.getDeleted());
        assertEquals(entity.getCreatedAt(), customer.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), customer.getUpdatedAt());
    }

    @Test
    void toCustomerJpaEntity_ShouldMapAllFields() {
        Customer customer = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("123")
                .deleted(false)
                .build();

        CustomerJpaEntity entity = customerMapper.toCustomerJpaEntity(customer);

        assertNotNull(entity);
        assertEquals(customer.getId(), entity.getId());
        assertEquals(customer.getCpf(), entity.getCpf());
        assertEquals(customer.getName(), entity.getName());
        assertEquals(customer.getPhone(), entity.getPhone());
        assertEquals(customer.getEmail(), entity.getEmail());
        assertEquals(customer.getCity(), entity.getCity());
        assertEquals(customer.getState(), entity.getState());
        assertEquals(customer.getDistrict(), entity.getDistrict());
        assertEquals(customer.getStreet(), entity.getStreet());
        assertEquals(customer.getNumber(), entity.getNumber());
        assertEquals(customer.getDeleted(), entity.getDeleted());
    }

}
