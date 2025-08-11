package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "O CPF não pode estar em branco.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
	private String cpf;
    
    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres.")
    private String password;
}
