package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "API para gerenciar clientes")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @Operation(summary = "Lista todos os clientes",
            description = "Retorna uma lista de todos os clientes cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/cpf")
    @Operation(summary = "Busca cliente pelo CPF",
            description = "Retorna o cliente correspondente ao CPF informado.")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
    public CustomerResponseDTO getCustomerByCpf(@RequestParam String cpf) {
        return customerService.getCustomerByCpf(cpf);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo cliente",
            description = "Registra um novo cliente na base de dados.")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public CustomerResponseDTO createCustomer(@RequestBody @Valid CustomerRequestDTO requestDTO) {
        return customerService.createCustomer(requestDTO);
    }

}
