package com.fiap.soat12.tc_group_7.dto.employee;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EmployeeRequestDTO {
    @NotNull(message = "O ID da função do funcionário não pode ser nulo.")
    private Long employeeFunctionId;

    @NotBlank(message = "O CPF não pode estar em branco.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    private String cpf;

    @NotBlank(message = "O nome não pode estar em branco.")
    @Size(max = 255, message = "O nome não pode exceder 255 caracteres.")
    private String name;

    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres.")
    private String password;

    @NotBlank(message = "O telefone não pode estar em branco.")
    @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
    private String phone;

    @NotBlank(message = "O e-mail não pode estar em branco.")
    @Email(message = "O e-mail deve ser válido.")
    @Size(max = 255, message = "O e-mail não pode exceder 255 caracteres.")
    private String email;

    private Boolean active = true;
}
