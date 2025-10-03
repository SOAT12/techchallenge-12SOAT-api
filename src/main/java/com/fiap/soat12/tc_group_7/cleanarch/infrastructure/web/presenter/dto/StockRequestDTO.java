package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisições de criação/atualização de Stock.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockRequestDTO {

    @NotBlank(message = "O nome da ferramenta não pode estar em branco.")
    @Size(max = 255, message = "O nome da ferramenta não pode exceder 255 caracteres.")
    private String toolName;

    @NotNull(message = "O valor não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal value;

    private Boolean active = true;

    @NotNull(message = "A quantidade não pode ser nula.")
    @Min(value = 0, message = "A quantidade não pode ser negativa.")
    private Integer quantity;

    @NotNull(message = "O ID da categoria da ferramenta não pode ser nulo.")
    private Long toolCategoryId;
}
