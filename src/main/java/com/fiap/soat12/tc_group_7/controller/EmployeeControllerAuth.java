package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import com.fiap.soat12.tc_group_7.service.AuthEmployeeService;
import com.fiap.soat12.tc_group_7.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Tag(name = "Funcionário", description = "API para gerenciar funcionários")
public class EmployeeControllerAuth {
    
    private final EmployeeService employeeService;
    private final AuthEmployeeService authEmployeeService;

    @Operation(summary = "Faz o login de um funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário faz login com sucesso")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado")
    @ApiResponse(responseCode = "401", description = "Credenciais não autorizadas")
    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) throws Exception {
        try {
            return new ResponseEntity<>(authEmployeeService.auth(requestDTO), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Operation(summary = "Altera a senha de um funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário altera senha com sucesso")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado")
    @ApiResponse(responseCode = "401", description = "Credenciais não autorizadas")
    @PutMapping(path = "/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequestDTO requestDTO) throws Exception {
        try {
            employeeService.changePassword(id, requestDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Esqueceu a senha de um funcionário")
    @ApiResponse(responseCode = "200", description = "Senha nova enviada ao email do funcionário")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado")
    @PostMapping(path = "/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) throws Exception {
        try {
            employeeService.forgotPassword(requestDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}