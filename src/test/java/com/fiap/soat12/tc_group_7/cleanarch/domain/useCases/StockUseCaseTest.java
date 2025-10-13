package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockUseCaseTest {

    @Mock
    private StockGateway stockGateway;

    @Mock
    private ToolCategoryGateway toolCategoryGateway;

    @InjectMocks
    private StockUseCase stockUseCase;

    private ToolCategory validCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validCategory = new ToolCategory(UUID.randomUUID(), "Hammers", true);
    }

    @Test
    void createStock_WithValidData_ShouldSaveAndReturnStock() {
        when(toolCategoryGateway.findById(validCategory.getId())).thenReturn(Optional.of(validCategory));
        when(stockGateway.findByName(anyString())).thenReturn(Optional.empty());
        when(stockGateway.save(any(Stock.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Stock result = stockUseCase.createStock("Sledgehammer", BigDecimal.TEN, 5, validCategory.getId());

        assertNotNull(result);
        assertEquals("Sledgehammer", result.getToolName());
        verify(stockGateway, times(1)).save(any(Stock.class));
    }

    @Test
    void createStock_WithExistingName_ShouldThrowException() {
        String toolName = "Sledgehammer";
        Stock existingStock = Stock.create(toolName, BigDecimal.ONE, 1, validCategory);
        when(toolCategoryGateway.findById(validCategory.getId())).thenReturn(Optional.of(validCategory));
        when(stockGateway.findByName(toolName)).thenReturn(Optional.of(existingStock));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            stockUseCase.createStock(toolName, BigDecimal.TEN, 5, validCategory.getId());
        });

        assertEquals("Item já cadastrado.", e.getMessage());
        verify(stockGateway, never()).save(any());
    }

    @Test
    void createStock_WithInvalidCategory_ShouldThrowException() {
        UUID invalidCategoryId = UUID.randomUUID();
        when(toolCategoryGateway.findById(invalidCategoryId)).thenReturn(Optional.empty());

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            stockUseCase.createStock("Tool", BigDecimal.TEN, 5, invalidCategoryId);
        });

        assertEquals("Categoria não encontrada", e.getMessage());
    }

    @Test
    void updateStockItem_WithValidData_ShouldUpdateAndSave() {
        UUID stockId = UUID.randomUUID();
        Stock existingStock = new Stock(stockId, "Old Name", BigDecimal.ONE, 10, validCategory, true, null, null);

        when(stockGateway.findById(stockId)).thenReturn(Optional.of(existingStock));
        when(toolCategoryGateway.findById(validCategory.getId())).thenReturn(Optional.of(validCategory));
        when(stockGateway.findByName("New Name")).thenReturn(Optional.empty());
        when(stockGateway.save(any(Stock.class))).thenReturn(existingStock);

        Stock result = stockUseCase.updateStockItem(stockId, "New Name", BigDecimal.TEN, 15, true, validCategory.getId());

        assertNotNull(result);
        assertEquals("New Name", result.getToolName());
        assertEquals(0, BigDecimal.TEN.compareTo(result.getValue()));
        assertEquals(15, result.getQuantity()); // 10 base + 5 added
        verify(stockGateway, times(1)).save(existingStock);
    }

    @Test
    void updateStockItem_WithExistingNameToDifferentItem_ShouldThrowException() {
        UUID stockId = UUID.randomUUID();
        String newName = "New Name";
        Stock existingStock = new Stock(stockId, "Old Name", BigDecimal.ONE, 10, validCategory, true, null, null);
        Stock otherStock = new Stock(UUID.randomUUID(), newName, BigDecimal.ONE, 1, validCategory, true, null, null);

        when(stockGateway.findById(stockId)).thenReturn(Optional.of(existingStock));
        when(stockGateway.findByName(newName)).thenReturn(Optional.of(otherStock));

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            stockUseCase.updateStockItem(stockId, newName, BigDecimal.TEN, 15, true, validCategory.getId());
        });

        assertEquals(String.format("O nome da ferramenta %s já está em uso.", newName), e.getMessage());
    }

    @Test
    void updateStockItem_NotFound_ShouldThrowException() {
        UUID stockId = UUID.randomUUID();
        when(stockGateway.findById(stockId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            stockUseCase.updateStockItem(stockId, "New Name", BigDecimal.TEN, 15, true, validCategory.getId());
        });
    }

    @Test
    void findStockItemById_WhenExists_ShouldReturnStock() {
        UUID stockId = UUID.randomUUID();
        Stock stock = new Stock(stockId, "FindMe", BigDecimal.ONE, 1, validCategory, true, null, null);
        when(stockGateway.findActiveById(stockId)).thenReturn(Optional.of(stock));

        Stock result = stockUseCase.findStockItemById(stockId);

        assertNotNull(result);
        assertEquals(stockId, result.getId());
    }

    @Test
    void findStockItemById_WhenNotExists_ShouldThrowException() {
        UUID stockId = UUID.randomUUID();
        when(stockGateway.findActiveById(stockId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockUseCase.findStockItemById(stockId));
    }

    @Test
    void inactivateStockItem_WhenExists_ShouldDeactivateAndSave() {
        UUID stockId = UUID.randomUUID();
        Stock stock = new Stock(stockId, "To Inactivate", BigDecimal.ONE, 1, validCategory, true, null, null);
        when(stockGateway.findById(stockId)).thenReturn(Optional.of(stock));

        stockUseCase.inactivateStockItem(stockId);

        assertFalse(stock.isActive());
        verify(stockGateway, times(1)).save(stock);
    }

    @Test
    void inactivateStockItem_WhenNotExists_ShouldThrowException() {
        UUID stockId = UUID.randomUUID();
        when(stockGateway.findById(stockId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockUseCase.inactivateStockItem(stockId));
    }

    @Test
    void reactivateStockItem_WhenExists_ShouldActivateAndSave() {
        UUID stockId = UUID.randomUUID();
        Stock stock = new Stock(stockId, "To Reactivate", BigDecimal.ONE, 1, validCategory, false, null, null);
        when(stockGateway.findById(stockId)).thenReturn(Optional.of(stock));

        stockUseCase.reactivateStockItem(stockId);

        assertTrue(stock.isActive());
        verify(stockGateway, times(1)).save(stock);
    }

    @Test
    void reactivateStockItem_WhenNotExists_ShouldThrowException() {
        UUID stockId = UUID.randomUUID();
        when(stockGateway.findById(stockId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> stockUseCase.reactivateStockItem(stockId));
    }
}
