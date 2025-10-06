package com.fiap.soat12.tc_group_7.cleanarch.restcontroller;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.service.EmployeeFunctionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-functions")
@RequiredArgsConstructor
@Tag(name = "Função de Funcionário", description = "API para gerenciar funções de funcionários")
public class EmployeeFunctionRestController {
    private final EmployeeFunctionService employeeFunctionService;

    @Operation(summary = "Cria uma nova função de funcionário",
            description = "Cria uma nova função de funcionário com base nos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Função criada com sucesso")
    @PostMapping
    public ResponseEntity<EmployeeFunctionResponseDTO> create(@Valid @RequestBody EmployeeFunctionRequestDTO requestDTO) {
        EmployeeFunctionResponseDTO created = employeeFunctionService.createEmployeeFunction(requestDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtém uma função de funcionário pelo ID",
            description = "Retorna uma função de funcionário específica pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Função encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeFunctionResponseDTO> getById(@PathVariable Long id) {
        return employeeFunctionService.getEmployeeFunctionById(id)
                .map(func -> new ResponseEntity<>(func, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lista todas as funções de funcionário ativas",
            description = "Retorna uma lista de todas as funções de funcionário cadastradas e com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de funções ativas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<EmployeeFunctionResponseDTO>> getAll() {
        List<EmployeeFunctionResponseDTO> list = employeeFunctionService.getAllEmployeeFunctions();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "Lista todas as funções de funcionário, incluindo inativas",
            description = "Retorna uma lista de todas as funções de funcionário cadastradas, independentemente do status.")
    @ApiResponse(responseCode = "200", description = "Lista de funções retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeFunctionResponseDTO>> getAllIncludingInactive() {
        List<EmployeeFunctionResponseDTO> list = employeeFunctionService.getAllEmployeeFunctionsIncludingInactive();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza uma função de funcionário existente",
            description = "Atualiza os dados de uma função de funcionário pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Função atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeFunctionResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeFunctionRequestDTO requestDTO) {
        return employeeFunctionService.updateEmployeeFunction(id, requestDTO)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Deleta logicamente uma função de funcionário",
            description = "Inativa uma função de funcionário pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Função inativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (employeeFunctionService.inactivateEmployeeFunction(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reativa uma função de funcionário",
            description = "Reativa uma função de funcionário que foi inativada.")
    @ApiResponse(responseCode = "204", description = "Função reativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Função não encontrada")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        if (employeeFunctionService.activateEmployeeFunction(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
