package com.fiap.soat12.tc_group_7.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respostas de ToolCategory.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

	private String cpf;
    private String password;
}
