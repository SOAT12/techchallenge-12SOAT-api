package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.CustomerController;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomerApiTest {

    @Mock
    private CustomerController customerController;

    @InjectMocks
    private CustomerApi customerApi;

    private CustomerRequestDTO sampleRequest;
    private CustomerResponseDTO sampleResponse;

    @BeforeEach
    void setUp() {
        sampleRequest = CustomerRequestDTO.builder()
                .cpf("12345678909")
                .name("João da Silva")
                .phone("11999999999")
                .email("joao@example.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("100")
                .build();

        sampleResponse = CustomerResponseDTO.builder()
                .id(1L)
                .cpf(sampleRequest.getCpf())
                .name(sampleRequest.getName())
                .phone(sampleRequest.getPhone())
                .email(sampleRequest.getEmail())
                .city(sampleRequest.getCity())
                .state(sampleRequest.getState())
                .district(sampleRequest.getDistrict())
                .street(sampleRequest.getStreet())
                .number(sampleRequest.getNumber())
                .deleted(false)
                .build();
    }

    @Test
    void getAllActiveCustomers_ShouldReturnList() {
        List<CustomerResponseDTO> mockList = List.of(sampleResponse);

        Mockito.when(customerController.getAllActiveCustomers()).thenReturn(mockList);

        List<CustomerResponseDTO> result = customerApi.getAllActiveCustomers();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("João da Silva", result.get(0).getName());
        Mockito.verify(customerController).getAllActiveCustomers();
    }

    @Test
    void getAllCustomers_ShouldReturnList() {
        List<CustomerResponseDTO> mockList = List.of(sampleResponse);

        Mockito.when(customerController.getAllCustomers()).thenReturn(mockList);

        List<CustomerResponseDTO> result = customerApi.getAllCustomers();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("12345678909", result.get(0).getCpf());
        Mockito.verify(customerController).getAllCustomers();
    }

    @Test
    void getCustomerByCpf_ShouldReturnCustomer() {
        String cpf = "12345678909";

        Mockito.when(customerController.getCustomerByCpf(cpf)).thenReturn(sampleResponse);

        CustomerResponseDTO result = customerApi.getCustomerByCpf(cpf);

        Assertions.assertEquals(cpf, result.getCpf());
        Assertions.assertEquals("João da Silva", result.getName());
        Mockito.verify(customerController).getCustomerByCpf(cpf);
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() {
        Mockito.when(customerController.createCustomer(sampleRequest)).thenReturn(sampleResponse);

        CustomerResponseDTO result = customerApi.createCustomer(sampleRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("João da Silva", result.getName());
        Assertions.assertEquals("joao@example.com", result.getEmail());
        Mockito.verify(customerController).createCustomer(sampleRequest);
    }

    @Test
    void updateCustomerById_ShouldReturnUpdatedCustomer() {
        Long id = 1L;

        CustomerRequestDTO updateRequest = CustomerRequestDTO.builder()
                .cpf("12345678909")
                .name("João Atualizado")
                .phone("11888888888")
                .email("joao.novo@example.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua B")
                .number("200")
                .build();

        CustomerResponseDTO updatedResponse = CustomerResponseDTO.builder()
                .id(id)
                .cpf(updateRequest.getCpf())
                .name(updateRequest.getName())
                .phone(updateRequest.getPhone())
                .email(updateRequest.getEmail())
                .city(updateRequest.getCity())
                .state(updateRequest.getState())
                .district(updateRequest.getDistrict())
                .street(updateRequest.getStreet())
                .number(updateRequest.getNumber())
                .deleted(false)
                .build();

        Mockito.when(customerController.updateCustomerById(id, updateRequest)).thenReturn(updatedResponse);

        CustomerResponseDTO result = customerApi.updateCustomerById(id, updateRequest);

        Assertions.assertEquals("João Atualizado", result.getName());
        Assertions.assertEquals("joao.novo@example.com", result.getEmail());
        Mockito.verify(customerController).updateCustomerById(id, updateRequest);
    }

    @Test
    void deleteCustomerById_ShouldCallController() {
        Long id = 1L;

        customerApi.deleteCustomerById(id);

        Mockito.verify(customerController).deleteCustomerById(id);
    }

    @Test
    void activateCustomer_ShouldCallController() {
        Long id = 1L;

        customerApi.activateCustomer(id);

        Mockito.verify(customerController).activateCustomer(id);
    }

}
