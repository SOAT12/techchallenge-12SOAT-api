package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
