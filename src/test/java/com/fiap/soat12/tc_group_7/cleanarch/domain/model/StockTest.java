package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    private ToolCategory validCategory;

    @BeforeEach
    void setUp() {
        validCategory = new ToolCategory(UUID.randomUUID(), "Screwdrivers", true);
    }

    @Test
    void create_WithValidData_ShouldReturnStockInstance() {
        Stock stock = Stock.create("Phillips #2", BigDecimal.TEN, 100, validCategory);

        assertNotNull(stock);
        assertEquals("Phillips #2", stock.getToolName());
        assertEquals(0, BigDecimal.TEN.compareTo(stock.getValue()));
        assertEquals(100, stock.getQuantity());
        assertEquals(validCategory, stock.getToolCategory());
        assertTrue(stock.isActive());
        assertNotNull(stock.getCreatedAt());
        assertNotNull(stock.getUpdatedAt());
    }

    @Test
    void create_WithNullName_ShouldThrowException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            Stock.create(null, BigDecimal.TEN, 100, validCategory);
        });
        assertEquals("Nome do item não pode ser nulo ou vazio.", e.getMessage());
    }

    @Test
    void create_WithNegativeValue_ShouldThrowException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            Stock.create("Tool", new BigDecimal("-1"), 100, validCategory);
        });
        assertEquals("Valor não pode ser nulo ou menor que zero.", e.getMessage());
    }

    @Test
    void create_WithNegativeQuantity_ShouldThrowException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            Stock.create("Tool", BigDecimal.TEN, -1, validCategory);
        });
        assertEquals("Quantidade não pode ser nula ou menor que zero.", e.getMessage());
    }

    @Test
    void create_WithNullCategory_ShouldThrowException() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            Stock.create("Tool", BigDecimal.TEN, 100, null);
        });
        assertEquals("Uma categoria válida é necessária.", e.getMessage());
    }

    @Test
    void addStock_WithPositiveQuantity_ShouldIncreaseQuantity() {
        Stock stock = Stock.create("Hammer", BigDecimal.valueOf(25), 10, validCategory);
        stock.addStock(5);
        assertEquals(15, stock.getQuantity());
    }

    @Test
    void addStock_WithNegativeQuantity_ShouldThrowException() {
        Stock stock = Stock.create("Hammer", BigDecimal.valueOf(25), 10, validCategory);
        Exception e = assertThrows(IllegalArgumentException.class, () -> stock.addStock(-1));
        assertEquals("Quantidade não pode ser nula ou menor do que zero.", e.getMessage());
    }

    @Test
    void removingStock_WithSufficientQuantity_ShouldDecreaseQuantity() {
        Stock stock = Stock.create("Wrench", BigDecimal.valueOf(15), 20, validCategory);
        stock.removingStock(15);
        assertEquals(5, stock.getQuantity());
    }

    @Test
    void removingStock_WithInsufficientQuantity_ShouldThrowException() {
        Stock stock = Stock.create("Wrench", BigDecimal.valueOf(15), 20, validCategory);
        Exception e = assertThrows(IllegalArgumentException.class, () -> stock.removingStock(21));
        assertEquals("Quantidade em estoque menor do que a informada.", e.getMessage());
    }

    @Test
    void removingStock_WithZeroQuantity_ShouldThrowException() {
        Stock stock = Stock.create("Wrench", BigDecimal.valueOf(15), 20, validCategory);
        Exception e = assertThrows(IllegalArgumentException.class, () -> stock.removingStock(0));
        assertEquals("Quantidade não pode ser nula ou menor do que zero.", e.getMessage());
    }

    @Test
    void deactivate_WhenActive_ShouldBecomeInactive() {
        Stock stock = Stock.create("Pliers", BigDecimal.ONE, 1, validCategory);
        stock.deactivate();
        assertFalse(stock.isActive());
    }

    @Test
    void deactivate_WhenAlreadyInactive_ShouldThrowException() {
        Stock stock = Stock.create("Pliers", BigDecimal.ONE, 1, validCategory);
        stock.deactivate();
        Exception e = assertThrows(IllegalStateException.class, stock::deactivate);
        assertEquals("Item já encontra-se desativado.", e.getMessage());
    }

    @Test
    void activate_WhenInactive_ShouldBecomeActive() {
        Stock stock = Stock.create("Pliers", BigDecimal.ONE, 1, validCategory);
        stock.deactivate();
        assertFalse(stock.isActive());
        stock.activate();
        assertTrue(stock.isActive());
    }

    @Test
    void activate_WhenAlreadyActive_ShouldThrowException() {
        Stock stock = Stock.create("Pliers", BigDecimal.ONE, 1, validCategory);
        Exception e = assertThrows(IllegalArgumentException.class, stock::activate);
        assertEquals("Item já encontra-se ativado.", e.getMessage());
    }

    @Test
    void updateDetails_WithValidData_ShouldUpdateFields() {
        Stock stock = Stock.create("Old Name", BigDecimal.ONE, 1, validCategory);
        ToolCategory newCategory = new ToolCategory(UUID.randomUUID(), "New Category", true);
        stock.updateDetails("New Name", BigDecimal.TEN, true, newCategory);

        assertEquals("New Name", stock.getToolName());
        assertEquals(0, BigDecimal.TEN.compareTo(stock.getValue()));
        assertEquals(newCategory, stock.getToolCategory());
        assertTrue(stock.isActive());
    }

    @Test
    void updateDetails_WithNullName_ShouldThrowException() {
        Stock stock = Stock.create("Old Name", BigDecimal.ONE, 1, validCategory);
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            stock.updateDetails(null, BigDecimal.TEN, true, validCategory);
        });
        assertEquals("Nome do item não pode ser nulo.", e.getMessage());
    }

    @Test
    void equals_and_hashCode_Contract() {
        UUID id = UUID.randomUUID();
        Stock stock1 = new Stock(id, "Test", BigDecimal.TEN, 10, validCategory, true, null, null);
        Stock stock2 = new Stock(id, "Test", BigDecimal.TEN, 10, validCategory, true, null, null);
        Stock stock3 = new Stock(UUID.randomUUID(), "Test", BigDecimal.TEN, 10, validCategory, true, null, null);

        assertEquals(stock1, stock2);
        assertNotEquals(stock1, stock3);
        assertEquals(stock1.hashCode(), stock2.hashCode());
        assertNotEquals(stock1.hashCode(), stock3.hashCode());
    }
}
