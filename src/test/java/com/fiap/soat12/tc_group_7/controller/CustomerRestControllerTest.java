package com.fiap.soat12.tc_group_7.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CustomerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void getAllActiveCustomers_withSuccess() throws Exception {
        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("João Silva")
                .phone("99999-9999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("123")
                .build();

        when(customerService.getAllActiveCustomers()).thenReturn(List.of(customerResponseDTO));

        mockMvc.perform(get("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[0].name").value("João Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    void getAllCustomers_withSuccess() throws Exception {
        CustomerResponseDTO customerResponseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("João Silva")
                .phone("99999-9999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua A")
                .number("123")
                .build();

        when(customerService.getAllCustomers()).thenReturn(List.of(customerResponseDTO));

        mockMvc.perform(get("/api/customers/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cpf").value("123.456.789-00"))
                .andExpect(jsonPath("$[0].name").value("João Silva"))
                .andExpect(jsonPath("$[0].email").value("joao@email.com"));
    }

    @Test
    void getCustomerByCpf_withSuccess() throws Exception {
        String cpf = "123.456.789-00";

        CustomerResponseDTO dto = CustomerResponseDTO.builder()
                .id(1L)
                .cpf(cpf)
                .name("João")
                .build();

        when(customerService.getCustomerByCpf(cpf)).thenReturn(dto);

        mockMvc.perform(get("/api/customers/by-cpf")
                        .param("cpf", cpf))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.cpf").value(dto.getCpf()))
                .andExpect(jsonPath("$.name").value(dto.getName()));
    }

    @Test
    void createCustomer_withSuccess() throws Exception {
        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .cpf("111.444.777-35")
                .name("João da Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua das Flores")
                .number("100")
                .build();
        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
                .id(1L)
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .district(requestDTO.getDistrict())
                .street(requestDTO.getStreet())
                .number(requestDTO.getNumber())
                .build();

        when(customerService.createCustomer(requestDTO)).thenReturn(responseDTO);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value("111.444.777-35"))
                .andExpect(jsonPath("$.name").value("João da Silva"));
    }

    @Test
    void updateCustomerById_withSuccess() throws Exception {
        // Arrange
        Long id = 1L;
        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .cpf("111.444.777-35")
                .name("João da Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua das Flores")
                .number("100")
                .build();
        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
                .id(id)
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .district(requestDTO.getDistrict())
                .street(requestDTO.getStreet())
                .number(requestDTO.getNumber())
                .build();

        when(customerService.updateCustomerById(eq(id), any(CustomerRequestDTO.class)))
                .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/customers/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("João da Silva"));

        verify(customerService).updateCustomerById(eq(id), any(CustomerRequestDTO.class));
    }

    @Test
    void deleteCustomerById_withSuccess() throws Exception {
        // Arrange
        Long id = 1L;

        doNothing().when(customerService).deleteCustomerById(id);

        // Act & Assert
        mockMvc.perform(delete("/api/customers/{id}", id))
                .andExpect(status().isOk())
                .andDo(print());
        verify(customerService).deleteCustomerById(id);
    }

    @Test
    void activateCustomer_withSuccess() throws Exception {
        // Arrange
        Long id = 1L;

        doNothing().when(customerService).activateCustomer(id);

        // Act & Assert
        mockMvc.perform(put("/api/customers/{id}/activate", id))
                .andExpect(status().isOk())
                .andDo(print());
        verify(customerService).activateCustomer(id);
    }

}
