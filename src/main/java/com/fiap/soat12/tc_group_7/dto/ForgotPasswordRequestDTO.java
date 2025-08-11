package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequestDTO {
    
    @NotBlank(message = "O CPF não pode estar em branco.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
	private String cpf;

}
