package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.EmployeeController;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Funcionário", description = "API para gerenciar funcionários")
public class EmployeeRestController {

    private final EmployeeController employeeController;

    @Operation(summary = "Cria um novo funcionário")
    @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso")
    @PostMapping
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody EmployeeRequestDTO requestDTO) {
        try {
            return employeeController.createEmployee(requestDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém um funcionário pelo ID")
    @ApiResponse(responseCode = "200", description = "Funcionário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @GetMapping("/{id}")
    public EmployeeResponseDTO getEmployeeById(@PathVariable Long id) {
        return employeeController.getEmployeeById(id);
    }

    @Operation(summary = "Lista todos os funcionários")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    @GetMapping("/all")
    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeController.getAllEmployees();
    }

    @Operation(summary = "Lista todos os funcionários ativos",
            description = "Retorna uma lista de todos os funcionários cadastrados e com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários ativos retornada com sucesso")
    @GetMapping
    public List<EmployeeResponseDTO> getAllActiveEmployees() {
        return employeeController.getAllActiveEmployees();
    }

    @Operation(summary = "Atualiza um funcionário existente")
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @PutMapping("/{id}")
    public EmployeeResponseDTO updateEmployeeById(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO requestDTO) {
        try {
            return employeeController.updateEmployeeById(id, requestDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta logicamente um funcionário",
            description = "Inativa logicamente um funcionário pelo seu ID. O funcionário não será removido do banco de dados, apenas ficará inativo.")
    @ApiResponse(responseCode = "204", description = "Funcionário inativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeController.inactivateEmployee(id);
    }

    @Operation(summary = "Reativa um funcionário logicamente inativado",
            description = "Reativa um funcionário que foi inativado logicamente, tornando-o novamente ativo no sistema.")
    @ApiResponse(responseCode = "204", description = "Funcionário reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @PutMapping("/{id}/activate")
    public void activateEmployee(@PathVariable Long id) {
        employeeController.activateEmployee(id);
    }
}
