package com.fiap.soat12.tc_group_7.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.service.AuthEmployeeService;
import com.fiap.soat12.tc_group_7.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Login", description = "API para login de funcionarios")
public class LoginController {
    private final AuthEmployeeService authEmployeeService;
    
    private final EmployeeService employeeService;
    
    @Operation(summary = "Faz o login de um funcionário")
    @ApiResponse(responseCode = "200", description = "Funcionário faz login com sucesso")
    @PostMapping(path = "/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO requestDTO) throws Exception {
    	System.out.println("teste");
    	return new ResponseEntity<>(authEmployeeService.auth(requestDTO), HttpStatus.OK);
	}

}
