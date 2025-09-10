package com.fiap.soat12.tc_group_7.api.application.port.in;

import com.fiap.soat12.tc_group_7.api.domain.model.Stock;
import com.fiap.soat12.tc_group_7.api.domain.model.ToolCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public interface CreateStockUseCase {

    Stock createStock(CreateStockCommand stock);

    record CreateStockCommand(
            @NotBlank(message = "The toolname cannot be blank.")
            String toolName,

            @NotNull(message = "The value cannot be null.")
            @Min(value = 0L, message = "The value cannot be fewer than zero.")
            BigDecimal value,

            @NotNull(message = "The quantity cannot be null.")
            @Min(value = 0, message = "The quantity cannot be fewer than zero.")
            Integer quantity,

            @NotNull(message = "The toolCategory cannot be null.")
            ToolCategory toolCategory
    ) {

        public CreateStockCommand {
            if (toolName != null && toolName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be only whitespace.");
            }
        }
    }
}
