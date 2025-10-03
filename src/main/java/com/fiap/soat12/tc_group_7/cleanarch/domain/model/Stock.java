package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Stock {

    private final UUID id;
    private String toolName;
    private BigDecimal value;
    private Integer quantity;
    private ToolCategory toolCategory;
    private Boolean isActive = true;
    private final Date createdAt;
    private Date updatedAt;

    /* CONSTRUCTORS */
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
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public Stock(UUID id, String toolName, BigDecimal value, Integer quantity, ToolCategory toolCategory, Boolean isActive, Date createdAt, Date updatedAt) {
        this.id = id;
        this.toolName = toolName;
        this.value = value;
        this.quantity = quantity;
        this.toolCategory = toolCategory;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /* BUSINESS LOGIC METHODS */
    public void addStock(Integer newQuantity) {
        if(Objects.isNull(newQuantity) ||  newQuantity < 0) {
            throw new IllegalArgumentException("A tool quantity must not be null or fewer than zero");
        }
        this.quantity += newQuantity;
        this.isActive = true;
        this.updatedAt = new Date();
    }

    public void removingStock(Integer removingQuantity){
        if(Objects.isNull(removingQuantity) || removingQuantity < 1) {
            throw new IllegalArgumentException("A tool quantity must not be null or fewer than one");
        }
        if(removingQuantity > this.quantity) {
            throw new IllegalArgumentException("A tool quantity must not be greater than the current quantity");
        }
        this.quantity -= removingQuantity;
        this.updatedAt = new Date();
    }


    public void deactivate() {
        if (Boolean.FALSE.equals(this.isActive)) {
            throw new IllegalStateException("Cannot deactivate an stock who is already inactive.");
        }
        this.isActive = false;
        this.updatedAt = new Date();
    }

    public void updateDetails(String newName, BigDecimal newValue, ToolCategory newCategory){
        changeName(newName);
        changeValue(newValue);
        changeCategory(newCategory);
        this.isActive = true;
        this.updatedAt = new Date();
    }

    private void changeCategory(ToolCategory newToolCategory) {
        if(Objects.isNull(newToolCategory)) {
            throw new IllegalArgumentException("New tool category cannot be null.");
        }
        this.toolCategory = newToolCategory;
        this.updatedAt = new Date();
    }

    private void changeName(String newName) {
        if(Objects.isNull(newName)) {
            throw new IllegalArgumentException("New name cannot be null.");
        }
        this.toolName = newName;
        this.updatedAt = new Date();

    }

    private void changeValue(BigDecimal newValue) {
        if(Objects.isNull(newValue) || newValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("New value cannot be null or negative.");
        }
        this.value = newValue;
        this.updatedAt = new Date();
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

    public Date getCreatedAt() { return createdAt; }

    public Date getUpdatedAt() { return updatedAt; }

    // --- DEFAULT OBJECT METHODS (equals, hashCode, toString) ---
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
                ", createdAt=" + createdAt + '\'' +
                ", updatedAt=" + updatedAt + '\'' +
                '}';
    }
}
