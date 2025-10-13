package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.CustomerController;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Cliente", description = "API para gerenciar clientes")
public class CustomerRestController {

    private final CustomerController customerController;

    @GetMapping
    @Operation(summary = "Lista todos os clientes ativos",
            description = "Retorna uma lista de todos os clientes ativos cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public List<CustomerResponseDTO> getAllActiveCustomers() {
        return customerController.getAllActiveCustomers();
    }

    @GetMapping("/all")
    @Operation(summary = "Lista todos os clientes",
            description = "Retorna uma lista de todos os clientes cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerController.getAllCustomers();
    }

    @GetMapping("/by-cpf")
    @Operation(summary = "Busca cliente pelo CPF",
            description = "Retorna o cliente correspondente ao CPF informado.")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso")
    public CustomerResponseDTO getCustomerByCpf(@RequestParam String cpf) {
        return customerController.getCustomerByCpf(cpf);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo cliente",
            description = "Registra um novo cliente na base de dados.")
    @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public CustomerResponseDTO createCustomer(@RequestBody @Valid CustomerRequestDTO requestDTO) {
        return customerController.createCustomer(requestDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um cliente pelo ID",
            description = "Atualiza os dados do cliente correspondente ao ID informado.")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public CustomerResponseDTO updateCustomerById(
            @PathVariable Long id,
            @RequestBody @Valid CustomerRequestDTO requestDTO) {
        return customerController.updateCustomerById(id, requestDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deleta um cliente pelo ID",
            description = "Marca o cliente como deletado no banco de dados."
    )
    @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public void deleteCustomerById(@PathVariable Long id) {
        customerController.deleteCustomerById(id);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Reativa um cliente logicamente inativado",
            description = "Reativa um cliente que foi inativado logicamente, tornando-o novamente ativo no sistema.")
    @ApiResponse(responseCode = "204", description = "Cliente reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public void activateCustomer(@PathVariable Long id) {
        customerController.activateCustomer(id);
    }

}
