package com.fiap.soat12.tc_group_7.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.service.AuthEmployeeService;
import com.fiap.soat12.tc_group_7.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Funcionário", description = "API para gerenciar funcionários")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final AuthEmployeeService authEmployeeService;

    @Operation(summary = "Cria um novo funcionário")
    @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso")
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(@Valid @RequestBody EmployeeRequestDTO requestDTO) {
        try {
            EmployeeResponseDTO created = employeeService.createEmployee(requestDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém um funcionário pelo ID")
    @ApiResponse(responseCode = "200", description = "Funcionário encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(@PathVariable Long id) {
    	System.out.println(1);
        return employeeService.getEmployeeById(id)
                .map(employee -> new ResponseEntity<>(employee, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lista todos os funcionários")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeResponseDTO>> getAllEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Lista todos os funcionários ativos",
            description = "Retorna uma lista de todos os funcionários cadastrados e com status ativo.")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários ativos retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<EmployeeResponseDTO>> getAllActiveEmployees() {
        List<EmployeeResponseDTO> employees = employeeService.getAllActiveEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um funcionário existente")
    @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO requestDTO) {
        try {
            return employeeService.updateEmployee(id, requestDTO)
                    .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta logicamente um funcionário",
            description = "Inativa logicamente um funcionário pelo seu ID. O funcionário não será removido do banco de dados, apenas ficará inativo.")
    @ApiResponse(responseCode = "204", description = "Funcionário inativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (employeeService.inactivateEmployee(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reativa um funcionário logicamente inativado",
            description = "Reativa um funcionário que foi inativado logicamente, tornando-o novamente ativo no sistema.")
    @ApiResponse(responseCode = "204", description = "Funcionário reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activateEmployee(@PathVariable Long id) {
        if (employeeService.activateEmployee(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @Operation(summary = "Faz o login de um funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário faz login com sucesso")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado")
    @ApiResponse(responseCode = "401", description = "Credenciais não autorizadas")
    @PostMapping(path = "/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO requestDTO) throws Exception {
    	return new ResponseEntity<>(authEmployeeService.auth(requestDTO), HttpStatus.OK);
	}
    
    @Operation(summary = "Altera a senha de um funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário altera senha com sucesso")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado")
    @ApiResponse(responseCode = "401", description = "Credenciais não autorizadas")
    @PutMapping(path = "/{id}/change-password")
	public void changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequestDTO requestDTO) throws Exception {
    	employeeService.changePassword(id, requestDTO);
	}
    
    @Operation(summary = "Esqueceu a senha de um funcionário")
    @ApiResponse(responseCode = "200", description = "Senha nova enviada ao email do funcionário")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado")
    @PostMapping(path = "/forgot-password")
	public void forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) throws Exception {
    	employeeService.forgotPassword(requestDTO);
	}
}
