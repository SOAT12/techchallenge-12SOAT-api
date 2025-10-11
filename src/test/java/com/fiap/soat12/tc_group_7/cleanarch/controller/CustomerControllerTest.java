package com.fiap.soat12.tc_group_7.cleanarch.controller;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.CustomerUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.CustomerController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.CustomerPresenter;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    private CustomerUseCase customerUseCase;
    private CustomerPresenter customerPresenter;
    private CustomerController controller;

    @BeforeEach
    void setup() {
        customerUseCase = mock(CustomerUseCase.class);
        customerPresenter = new CustomerPresenter();
        controller = new CustomerController(customerUseCase, customerPresenter);
    }

    @Test
    void getAllActiveCustomers_shouldReturnMappedDTOs() {
        var customers = List.of(
                Customer.builder().id(1L).cpf("111").name("João").deleted(false).build(),
                Customer.builder().id(2L).cpf("222").name("Maria").deleted(false).build()
        );
        when(customerUseCase.getAllActiveCustomers()).thenReturn(customers);

        List<CustomerResponseDTO> dtos = controller.getAllActiveCustomers();

        assertEquals(2, dtos.size());
        assertEquals(customers.get(0).getId(), dtos.get(0).getId());
        assertEquals(customers.get(1).getCpf(), dtos.get(1).getCpf());

        verify(customerUseCase, times(1)).getAllActiveCustomers();
    }

    @Test
    void getAllCustomers_shouldReturnMappedDTOs() {
        var customers = List.of(
                Customer.builder().id(3L).cpf("333").name("Ana").deleted(false).build()
        );
        when(customerUseCase.getAllCustomers()).thenReturn(customers);

        List<CustomerResponseDTO> dtos = controller.getAllCustomers();

        assertEquals(1, dtos.size());
        assertEquals("Ana", dtos.get(0).getName());

        verify(customerUseCase, times(1)).getAllCustomers();
    }

    @Test
    void getCustomerByCpf_shouldReturnMappedDTO() {
        Customer customer = Customer.builder().id(4L).cpf("444").name("Carlos").build();
        when(customerUseCase.getCustomerByCpf("444")).thenReturn(customer);

        CustomerResponseDTO dto = controller.getCustomerByCpf("444");

        assertNotNull(dto);
        assertEquals("Carlos", dto.getName());

        verify(customerUseCase, times(1)).getCustomerByCpf("444");
    }

    @Test
    void createCustomer_shouldReturnMappedDTO() {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();

        Customer createdCustomer = Customer.builder().id(5L).cpf("555").name("Daniel").build();
        when(customerUseCase.createCustomer(requestDTO)).thenReturn(createdCustomer);

        CustomerResponseDTO dto = controller.createCustomer(requestDTO);

        assertNotNull(dto);
        assertEquals("Daniel", dto.getName());

        verify(customerUseCase, times(1)).createCustomer(requestDTO);
    }

    @Test
    void updateCustomerById_shouldReturnMappedDTO() {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        Long id = 6L;

        Customer updatedCustomer = Customer.builder().id(id).cpf("666").name("Eva").build();
        when(customerUseCase.updateCustomerById(id, requestDTO)).thenReturn(updatedCustomer);

        CustomerResponseDTO dto = controller.updateCustomerById(id, requestDTO);

        assertNotNull(dto);
        assertEquals("Eva", dto.getName());

        verify(customerUseCase, times(1)).updateCustomerById(id, requestDTO);
    }

    @Test
    void deleteCustomerById_shouldCallUseCase() {
        Long id = 7L;

        controller.deleteCustomerById(id);

        verify(customerUseCase, times(1)).deleteCustomerById(id);
    }

    @Test
    void activateCustomer_shouldCallUseCase() {
        Long id = 8L;

        controller.activateCustomer(id);

        verify(customerUseCase, times(1)).activateCustomer(id);
    }

}
