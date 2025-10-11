package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.entity.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerGatewayTest {

    private CustomerRepository customerRepository;
    private CustomerGateway customerGateway;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerGateway = new CustomerGateway(customerRepository);
    }

    @Test
    void findAll_shouldReturnListOfCustomers() {
        List<CustomerJpaEntity> jpaEntities = List.of(createCustomerJpaEntity(1L), createCustomerJpaEntity(2L));
        when(customerRepository.findAll()).thenReturn(jpaEntities);

        List<Customer> result = customerGateway.findAll();

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void findById_shouldReturnCustomer_whenFound() {
        CustomerJpaEntity jpaEntity = createCustomerJpaEntity(1L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(jpaEntity));

        Optional<Customer> result = customerGateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(jpaEntity.getId(), result.get().getId());
        verify(customerRepository).findById(1L);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerGateway.findById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByCpf_shouldReturnCustomer_whenFound() {
        CustomerJpaEntity jpaEntity = createCustomerJpaEntity(1L);
        when(customerRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(jpaEntity));

        Optional<Customer> result = customerGateway.findByCpf("123.456.789-00");

        assertTrue(result.isPresent());
        assertEquals(jpaEntity.getCpf(), result.get().getCpf());
        verify(customerRepository).findByCpf("123.456.789-00");
    }

    @Test
    void save_shouldReturnCustomer_whenSavedSuccessfully() {
        Customer customer = createCustomer(1L);
        CustomerJpaEntity jpaEntity = createCustomerJpaEntity(1L);

        when(customerRepository.save(any(CustomerJpaEntity.class))).thenReturn(jpaEntity);

        Customer savedCustomer = customerGateway.save(customer);

        assertNotNull(savedCustomer);
        assertEquals(customer.getCpf(), savedCustomer.getCpf());
        verify(customerRepository).save(any(CustomerJpaEntity.class));
    }

    @Test
    void update_shouldCallSaveMethod() {
        Customer customer = createCustomer(1L);

        customerGateway.update(customer);

        verify(customerRepository).save(any(CustomerJpaEntity.class));
    }

    private CustomerJpaEntity createCustomerJpaEntity(Long id) {
        return CustomerJpaEntity.builder()
                .id(id)
                .cpf("123.456.789-00")
                .name("João Silva")
                .phone("99999-9999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("123")
                .deleted(false)
                .build();
    }

    private Customer createCustomer(Long id) {
        return Customer.builder()
                .id(id)
                .cpf("123.456.789-00")
                .name("João Silva")
                .phone("99999-9999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("123")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

}
