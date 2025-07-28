package com.fiap.soat12.tc_group_7.dto.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeFunctionRequestDTO {
    @NotBlank(message = "A descrição da função não pode estar em branco.")
    @Size(max = 100, message = "A descrição da função não pode exceder 100 caracteres.")
    private String description;
}
