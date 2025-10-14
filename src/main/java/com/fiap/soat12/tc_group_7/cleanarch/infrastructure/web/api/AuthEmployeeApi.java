package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.EmployeeAuthUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.PasswordManagementUseCase;
import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação de Funcionário", description = "API para Login e Gestão de Senhas (Clean Architecture)")
public class AuthEmployeeApi {

    private final EmployeeAuthUseCase employeeAuthUseCase;
    private final PasswordManagementUseCase passwordManagementUseCase;

    @Operation(summary = "Faz o login de um funcionário")
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido, token retornado")
    @ApiResponse(responseCode = "401", description = "Credenciais não autorizadas")
    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO requestDTO) throws Exception {
        return employeeAuthUseCase.auth(requestDTO);
    }

    @Operation(summary = "Altera a senha de um funcionário autenticado")
    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "401", description = "Senha antiga incorreta")
    @PutMapping(path = "/{id}/change-password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequestDTO requestDTO) throws Exception {
        passwordManagementUseCase.changePassword(id, requestDTO);
    }

    @Operation(summary = "Esqueceu a senha",
            description = "Gera uma senha temporária e a envia ao email do funcionário.")
    @ApiResponse(responseCode = "200", description = "Processo iniciado, senha temporária enviada")
    @ApiResponse(responseCode = "400", description = "Funcionário não encontrado ou erro no envio de e-mail")
    @PostMapping(path = "/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO requestDTO) throws Exception {
        passwordManagementUseCase.forgotPassword(requestDTO);
    }
}