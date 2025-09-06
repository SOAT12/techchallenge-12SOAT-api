package com.fiap.soat12.tc_group_7.api.domain.model;

import com.fiap.soat12.tc_group_7.entity.ToolCategory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Stock {

    private final UUID id;
    private String toolName;
    private BigDecimal value;
    private Integer quantity;
    private ToolCategory toolCategory;
    private Boolean isActive = true;

    public Stock(String toolName, BigDecimal value, Integer quantity, ToolCategory toolCategory) {
        if(toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("A tool name must not be null or blank");
        }
        if(value == null || value.compareTo(BigDecimal.ZERO) < 0 || quantity == null || quantity < 0) {
            throw new IllegalArgumentException("A tool value and  quantity must not be null or fewer than zero");
        }
        if(toolCategory == null) {
            throw new IllegalArgumentException("A toolCategory is required");
        }

        this.id = UUID.randomUUID();
        this.toolName = toolName;
        this.value = value;
        this.quantity = quantity;
        this.toolCategory = toolCategory;
        this.isActive = true;
    }

    public Stock(UUID id, String toolName, BigDecimal value, Integer quantity, ToolCategory toolCategory, Boolean isActive) {
        this.id = id;
        this.toolName = toolName;
        this.value = value;
        this.quantity = quantity;
        this.toolCategory = toolCategory;
        this.isActive = isActive;
    }

    /* BUSINESS LOGIC METHODS */

    public void deactivate() {
        if (Boolean.FALSE.equals(this.isActive)) {
            throw new IllegalStateException("Cannot deactivate an stock who is already inactive.");
        }
        this.isActive = false;
    }

    public void changeCategory(ToolCategory newToolCategory) {
        if(Objects.isNull(newToolCategory)) {
            throw new IllegalArgumentException("New tool category cannot be null.");
        }
        this.toolCategory = newToolCategory;
    }

    /* GETTERS AND SETTERS */

    public UUID getId() {
        return id;
    }

    public String getToolName() {
        return toolName;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public ToolCategory getToolCategory() {
        return toolCategory;
    }

    public Boolean isActive() {
        return isActive;
    }

    // --- Standard Object Methods (equals, hashCode, toString) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock stock = (Stock) o;
        return Objects.equals(id, stock.id); // Equality is based on the unique ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stock {" +
                "id=" + id +
                ", toolName='" + toolName + '\'' +
                ", value=" + value + '\'' +
                ", quantity=" + quantity + '\'' +
                ", toolCategory=" + toolCategory + '\'' +
                ", isActive=" + isActive + '\'' +
                '}';
    }
}
