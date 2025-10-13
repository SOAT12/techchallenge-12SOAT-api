package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.EmployeeFunctionController;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-functions")
@RequiredArgsConstructor
@Tag(name = "Função de Funcionário", description = "API para gerenciar funções de funcionários")
public class EmployeeFunctionRestController {
    private final EmployeeFunctionController employeeFunctionController;

    @Operation(summary = "Cria uma nova função de funcionário",
            description = "Cria uma nova função de funcionário com base nos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Função criada com sucesso")
    @PostMapping
    public EmployeeFunctionResponseDTO create(@Valid @RequestBody EmployeeFunctionRequestDTO requestDTO) {
        return employeeFunctionController.createEmployeeFunction(requestDTO);
    }

    @Operation(summary = "Obtém uma função de funcionário pelo ID",
            description = "Retorna uma função de funcionário específica pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Função encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @GetMapping("/{id}")
    public EmployeeFunctionResponseDTO getById(@PathVariable Long id) {
        return employeeFunctionController.getEmployeeFunctionById(id);
    }

    @Operation(summary = "Lista todas as funções de funcionário ativas",
            description = "Retorna uma lista de todas as funções de funcionário cadastradas e com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de funções ativas retornada com sucesso")
    @GetMapping
    public List<EmployeeFunctionResponseDTO> getAllActiveEmployeeFunctions() {
        return employeeFunctionController.getAllActiveEmployeeFunctions();
    }

    @Operation(summary = "Lista todas as funções de funcionário, incluindo inativas",
            description = "Retorna uma lista de todas as funções de funcionário cadastradas, independentemente do status.")
    @ApiResponse(responseCode = "200", description = "Lista de funções retornada com sucesso")
    @GetMapping("/all")
    public List<EmployeeFunctionResponseDTO> getAll() {
        return employeeFunctionController.getAllEmployeeFunctions();
    }

    @Operation(summary = "Atualiza uma função de funcionário existente",
            description = "Atualiza os dados de uma função de funcionário pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Função atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @PutMapping("/{id}")
    public EmployeeFunctionResponseDTO update(@PathVariable Long id, @Valid @RequestBody EmployeeFunctionRequestDTO requestDTO) {
        return employeeFunctionController.updateEmployeeFunctionById(id, requestDTO);
    }

    @Operation(summary = "Deleta logicamente uma função de funcionário",
            description = "Inativa uma função de funcionário pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Função inativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeFunctionController.inactivateEmployeeFunction(id);
    }

    @Operation(summary = "Reativa uma função de funcionário",
            description = "Reativa uma função de funcionário que foi inativada.")
    @ApiResponse(responseCode = "204", description = "Função reativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @PutMapping("/{id}/activate")
    public void activate(@PathVariable Long id) {
        employeeFunctionController.activateEmployeeFunction(id);
    }
}
