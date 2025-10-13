package com.fiap.soat12.tc_group_7.cleanarch.usercase;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.CustomerUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.CustomerGateway;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    private CustomerUseCase customerUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        customerUseCase = new CustomerUseCase(customerGateway);
    }

    @Test
    void getAllActiveCustomers_returnsOnlyNonDeleted() {
        Customer activeCustomer = Customer.builder().id(1L).deleted(false).build();
        Customer deletedCustomer = Customer.builder().id(2L).deleted(true).build();

        when(customerGateway.findAll()).thenReturn(List.of(activeCustomer, deletedCustomer));

        List<Customer> result = customerUseCase.getAllActiveCustomers();

        assertEquals(1, result.size());
        assertFalse(result.getFirst().getDeleted());
        verify(customerGateway).findAll();
    }

    @Test
    void getAllCustomers_returnsAll() {
        List<Customer> customers = List.of(
                Customer.builder().id(1L).build(),
                Customer.builder().id(2L).build()
        );

        when(customerGateway.findAll()).thenReturn(customers);

        List<Customer> result = customerUseCase.getAllCustomers();

        assertEquals(2, result.size());
        verify(customerGateway).findAll();
    }

    @Test
    void getCustomerByCpf_found() {
        String cpf = "12345678900";
        Customer customer = Customer.builder().cpf(cpf).build();

        when(customerGateway.findByCpf(cpf)).thenReturn(Optional.of(customer));

        Customer result = customerUseCase.getCustomerByCpf(cpf);

        assertEquals(cpf, result.getCpf());
        verify(customerGateway).findByCpf(cpf);
    }

    @Test
    void getCustomerByCpf_notFound_throwsException() {
        String cpf = "12345678900";

        when(customerGateway.findByCpf(cpf)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            customerUseCase.getCustomerByCpf(cpf);
        });

        assertEquals(CustomerUseCase.CUSTOMER_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(customerGateway).findByCpf(cpf);
    }

    @Test
    void createCustomer_savesAndReturnsCustomer() {
        CustomerRequestDTO dto = CustomerRequestDTO.builder()
                .cpf("12345678900")
                .name("João")
                .phone("99999-9999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .build();

        Customer savedCustomer = Customer.builder()
                .id(1L)
                .cpf(dto.getCpf())
                .name(dto.getName())
                .build();

        when(customerGateway.save(any(Customer.class))).thenReturn(savedCustomer);

        Customer result = customerUseCase.createCustomer(dto);

        assertEquals(savedCustomer.getId(), result.getId());
        assertEquals(dto.getCpf(), result.getCpf());
        verify(customerGateway).save(any(Customer.class));
    }

    @Test
    void updateCustomerById_found_updatesAndReturns() {
        Long id = 1L;
        Customer existingCustomer = Customer.builder()
                .id(id)
                .cpf("oldcpf")
                .build();

        CustomerRequestDTO dto = CustomerRequestDTO.builder()
                .cpf("newcpf")
                .name("New Name")
                .phone("12345")
                .email("new@email.com")
                .city("City")
                .state("ST")
                .district("District")
                .street("Street")
                .number("10")
                .build();

        when(customerGateway.findById(id)).thenReturn(Optional.of(existingCustomer));

        Customer result = customerUseCase.updateCustomerById(id, dto);

        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getName(), result.getName());
        verify(customerGateway).findById(id);
        verify(customerGateway).update(existingCustomer);
    }

    @Test
    void updateCustomerById_notFound_throwsException() {
        Long id = 1L;
        CustomerRequestDTO dto = CustomerRequestDTO.builder().build();

        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            customerUseCase.updateCustomerById(id, dto);
        });

        assertEquals(CustomerUseCase.CUSTOMER_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(customerGateway).findById(id);
        verify(customerGateway, never()).update(any());
    }

    @Test
    void deleteCustomerById_found_setsDeletedTrue() {
        Long id = 1L;
        Customer customer = Customer.builder().id(id).deleted(false).build();

        when(customerGateway.findById(id)).thenReturn(Optional.of(customer));

        customerUseCase.deleteCustomerById(id);

        assertTrue(customer.getDeleted());
        verify(customerGateway).update(customer);
    }

    @Test
    void deleteCustomerById_notFound_throwsException() {
        Long id = 1L;

        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            customerUseCase.deleteCustomerById(id);
        });

        assertEquals(CustomerUseCase.CUSTOMER_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(customerGateway).findById(id);
        verify(customerGateway, never()).update(any());
    }

    @Test
    void activateCustomer_found_setsDeletedFalse() {
        Long id = 1L;
        Customer customer = Customer.builder().id(id).deleted(true).build();

        when(customerGateway.findById(id)).thenReturn(Optional.of(customer));

        customerUseCase.activateCustomer(id);

        assertFalse(customer.getDeleted());
        verify(customerGateway).update(customer);
    }

    @Test
    void activateCustomer_notFound_throwsException() {
        Long id = 1L;

        when(customerGateway.findById(id)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            customerUseCase.activateCustomer(id);
        });

        assertEquals(CustomerUseCase.CUSTOMER_NOT_FOUND_MESSAGE, ex.getMessage());
        verify(customerGateway).findById(id);
        verify(customerGateway, never()).update(any());
    }

}
