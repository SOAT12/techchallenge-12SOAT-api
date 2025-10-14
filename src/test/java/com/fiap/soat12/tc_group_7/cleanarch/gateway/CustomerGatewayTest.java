package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerGatewayTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerGateway customerGateway;

    @BeforeEach
    void setUp() {
        customerGateway = new CustomerGateway(customerRepository);
    }

    @Test
    void findAll_shouldReturnListOfCustomers() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .cpf("12345678911")
                .name("João Souto")
                .phone("11999999999")
                .email("joaosouto@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Customer customer2 = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        List<Customer> customers = List.of(customer1, customer2);
        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> result = customerGateway.findAll();

        assertEquals(2, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void findById_shouldReturnCustomerOptional() {
        Long id = 1L;
        Customer customer = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerGateway.findById(id);

        assertTrue(result.isPresent());
        verify(customerRepository).findById(id);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotFound() {
        Long id = 1L;
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Customer> result = customerGateway.findById(id);

        assertFalse(result.isPresent());
        verify(customerRepository).findById(id);
    }

    @Test
    void findByCpf_shouldReturnCustomerOptional() {
        String cpf = "12345678900";
        Customer customer = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));

        Optional<Customer> result = customerGateway.findByCpf(cpf);

        assertTrue(result.isPresent());
        verify(customerRepository).findByCpf(cpf);
    }

    @Test
    void findByCpf_shouldReturnEmptyWhenNotFound() {
        String cpf = "12345678900";
        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        Optional<Customer> result = customerGateway.findByCpf(cpf);

        assertFalse(result.isPresent());
        verify(customerRepository).findByCpf(cpf);
    }

    @Test
    void save_shouldReturnSavedCustomer() {
        Customer customer = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(customerRepository.save(customer)).thenReturn(customer);

        Customer result = customerGateway.save(customer);

        assertNotNull(result);
        verify(customerRepository).save(customer);
    }

    @Test
    void update_shouldCallSaveOnRepository() {
        Customer customer = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .deleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        customerGateway.update(customer);

        verify(customerRepository).save(customer);
    }

}
