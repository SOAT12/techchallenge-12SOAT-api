package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.CustomerJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerRepositoryImplTest {

    @Mock
    private CustomerJpaRepository customerJpaRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerRepositoryImpl customerRepositoryImpl;

    private Customer customer;
    private CustomerJpaEntity customerJpaEntity;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = Customer.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João Silva")
                .build();

        customerJpaEntity = new CustomerJpaEntity();
        customerJpaEntity.setId(1L);
        customerJpaEntity.setCpf("12345678900");
        customerJpaEntity.setName("João Silva");
    }

    @Test
    void findAll_ShouldReturnMappedCustomers() {
        // Setup mock
        when(customerJpaRepository.findAll()).thenReturn(List.of(customerJpaEntity));
        when(customerMapper.toCustomer(customerJpaEntity)).thenReturn(customer);

        // Execute
        List<Customer> result = customerRepositoryImpl.findAll();

        // Verify and assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(customer, result.get(0));

        verify(customerJpaRepository).findAll();
        verify(customerMapper).toCustomer(customerJpaEntity);
    }

    @Test
    void findById_ShouldReturnMappedCustomer_WhenPresent() {
        when(customerJpaRepository.findById(1L)).thenReturn(Optional.of(customerJpaEntity));
        when(customerMapper.toCustomer(customerJpaEntity)).thenReturn(customer);

        Optional<Customer> result = customerRepositoryImpl.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());

        verify(customerJpaRepository).findById(1L);
        verify(customerMapper).toCustomer(customerJpaEntity);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(customerJpaRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Customer> result = customerRepositoryImpl.findById(2L);

        assertTrue(result.isEmpty());

        verify(customerJpaRepository).findById(2L);
        verifyNoInteractions(customerMapper);
    }

    @Test
    void findByCpf_ShouldReturnMappedCustomer_WhenPresent() {
        when(customerJpaRepository.findByCpf("12345678900")).thenReturn(Optional.of(customerJpaEntity));
        when(customerMapper.toCustomer(customerJpaEntity)).thenReturn(customer);

        Optional<Customer> result = customerRepositoryImpl.findByCpf("12345678900");

        assertTrue(result.isPresent());
        assertEquals(customer, result.get());

        verify(customerJpaRepository).findByCpf("12345678900");
        verify(customerMapper).toCustomer(customerJpaEntity);
    }

    @Test
    void findByCpf_ShouldReturnEmpty_WhenNotFound() {
        when(customerJpaRepository.findByCpf("00000000000")).thenReturn(Optional.empty());

        Optional<Customer> result = customerRepositoryImpl.findByCpf("00000000000");

        assertTrue(result.isEmpty());

        verify(customerJpaRepository).findByCpf("00000000000");
        verifyNoInteractions(customerMapper);
    }

    @Test
    void save_ShouldReturnMappedSavedCustomer() {
        when(customerMapper.toCustomerJpaEntity(customer)).thenReturn(customerJpaEntity);
        when(customerJpaRepository.save(customerJpaEntity)).thenReturn(customerJpaEntity);
        when(customerMapper.toCustomer(customerJpaEntity)).thenReturn(customer);

        Customer saved = customerRepositoryImpl.save(customer);

        assertNotNull(saved);
        assertEquals(customer, saved);

        verify(customerMapper).toCustomerJpaEntity(customer);
        verify(customerJpaRepository).save(customerJpaEntity);
        verify(customerMapper).toCustomer(customerJpaEntity);
    }

}
