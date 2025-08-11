package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequestDTO {
    
    @NotBlank(message = "A senha antiga não pode estar em branco.")
    @Size(min = 6, max = 255, message = "A senha antiga deve ter entre 6 e 255 caracteres.")
	private String oldPassword;
	
    @NotBlank(message = "A senha não pode estar em branco.")
    @Size(min = 6, max = 255, message = "A nova senha deve ter entre 6 e 255 caracteres.")
	private String newPassword;
	
    @NotBlank(message = "A comfirmação de senha não pode estar em branco.")
    @Size(min = 6, max = 255, message = "A comfirmação de senha deve ter entre 6 e 255 caracteres.")
	private String confirmationPassword;
}
