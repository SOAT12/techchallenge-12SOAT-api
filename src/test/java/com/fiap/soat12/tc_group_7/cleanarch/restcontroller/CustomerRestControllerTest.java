package com.fiap.soat12.tc_group_7.cleanarch.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.cleanarch.controller.CustomerController;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerController customerController;

    private CustomerResponseDTO customerResponseDTO;

    @BeforeEach
    void setUp() {
        customerResponseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .cpf("12345678900")
                .name("João da Silva")
                .email("joao@example.com")
                .build();
    }

    @Test
    void shouldReturnListOfActiveCustomers() throws Exception {
        List<CustomerResponseDTO> customers = List.of(customerResponseDTO);

        when(customerController.getAllActiveCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].cpf").value("12345678900"));
    }

    @Test
    void shouldReturnAllCustomers() throws Exception {
        List<CustomerResponseDTO> customers = List.of(customerResponseDTO);

        when(customerController.getAllCustomers()).thenReturn(customers);

        mockMvc.perform(get("/api/customers/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldReturnCustomerByCpf() throws Exception {
        when(customerController.getCustomerByCpf("12345678900")).thenReturn(customerResponseDTO);

        mockMvc.perform(get("/api/customers/by-cpf").param("cpf", "12345678900"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    @Test
    void shouldCreateCustomer() throws Exception {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        requestDTO.setCpf("12345678900");
        requestDTO.setName("João da Silva");
        requestDTO.setEmail("joao@example.com");

        when(customerController.createCustomer(any(CustomerRequestDTO.class))).thenReturn(customerResponseDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("João da Silva"));
    }

    @Test
    void shouldUpdateCustomer() throws Exception {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        requestDTO.setCpf("12345678900");
        requestDTO.setName("João Atualizado");
        requestDTO.setEmail("joao.atualizado@example.com");

        when(customerController.updateCustomerById(eq(1L), any(CustomerRequestDTO.class))).thenReturn(customerResponseDTO);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerController, times(1)).deleteCustomerById(1L);
    }

    @Test
    void shouldActivateCustomer() throws Exception {
        mockMvc.perform(put("/api/customers/1/activate"))
                .andExpect(status().isNoContent());

        verify(customerController, times(1)).activateCustomer(1L);
    }

}
