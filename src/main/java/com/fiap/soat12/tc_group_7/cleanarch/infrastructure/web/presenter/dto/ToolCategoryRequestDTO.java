package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisições de criação/atualização de ToolCategory.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolCategoryRequestDTO {

    @NotBlank(message = "O nome da categoria da ferramenta não pode estar em branco.")
    @Size(max = 255, message = "O nome da categoria da ferramenta não pode exceder 255 caracteres.")
    private String toolCategoryName;

}
