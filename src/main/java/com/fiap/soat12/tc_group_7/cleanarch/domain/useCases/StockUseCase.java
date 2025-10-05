package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.application.port.in.UpdateStockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@RequiredArgsConstructor
public class StockUseCase {

    private final StockGateway stockGateway;

    public Stock createStock(CreateStockCommand command) {
        if (stockGateway.findByName(command.toolName()).isPresent()) {

            throw new IllegalStateException("A stock item with the name '" +
                    command.toolName() + "' already exists.");
        }

        var newStock = new Stock(
                command.toolName(),
                command.value(),
                command.quantity(),
                command.toolCategory());

        return stockGateway.save(newStock);
    }

    public Stock updateStockItem(UpdateStockUseCase.UpdateStockItemCommand command) {
        Stock stockItem = stockGateway.findActiveById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Stock item not found with id: " + command.id()));

        stockItem.updateDetails(command.name(), command.value(), command.toolCategory());

        return stockGateway.save(stockItem);
    }

    public Stock findStockItemById(UUID id) {
        return stockGateway.findActiveById(id).orElseThrow(() -> new NotFoundException("Item de estoque não encontrado."));
    }

    public void inactivateStockItem(UUID id) {
        stockGateway.inactivateById(id);
    }

    public record CreateStockCommand(
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
