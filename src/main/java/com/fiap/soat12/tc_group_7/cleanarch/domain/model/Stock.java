package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Stock {

    private UUID id;
    private String toolName;
    private BigDecimal value;
    private Integer quantity;
    private ToolCategory toolCategory;
    private Boolean isActive = true;
    private final Date createdAt;
    private Date updatedAt;

    /* CONSTRUCTORS */
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

    private Stock(String toolName, BigDecimal value, Integer quantity, ToolCategory toolCategory, Boolean isActive, Date createdAt, Date updatedAt) {
        this.toolName = toolName;
        this.value = value;
        this.quantity = quantity;
        this.toolCategory = toolCategory;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /* FACTORY */
    public static Stock create(String toolName, BigDecimal value, Integer quantity, ToolCategory toolCategory) {
        if(toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException("Nome do item não pode ser nulo ou vazio.");
        }
        if(value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser nulo ou menor que zero.");
        }
        if(quantity == null || quantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser nula ou menor que zero.");
        }
        if(toolCategory == null) {
            throw new IllegalArgumentException("Uma categoria válida é necessária.");
        }

        return new Stock(toolName, value, quantity, toolCategory, true, new Date(), new Date());
    }

    /* BUSINESS LOGIC METHODS */
    public Stock addStock(Integer newQuantity) {
        if(Objects.isNull(newQuantity) ||  newQuantity < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser nula ou menor do que zero.");
        }
        this.quantity += newQuantity;
        this.isActive = true;
        this.updatedAt = new Date();

        return this;
    }

    public Stock removingStock(Integer removingQuantity){
        if(Objects.isNull(removingQuantity) || removingQuantity < 1) {
            throw new IllegalArgumentException("Quantidade não pode ser nula ou menor do que zero.");
        }
        if(removingQuantity > this.quantity) {
            throw new IllegalArgumentException("Quantidade em estoque menor do que a informada.");
        }
        this.quantity -= removingQuantity;
        this.updatedAt = new Date();

        return this;
    }


    public Stock deactivate() {
        if (Boolean.FALSE.equals(this.isActive)) {
            throw new IllegalStateException("Item já encontra-se desativado.");
        }
        this.isActive = false;
        this.updatedAt = new Date();

        return this;
    }

    public Stock activate() {
        if (Boolean.TRUE.equals(this.isActive)) {
            throw new IllegalArgumentException("Item já encontra-se ativado.");
        }
        this.isActive = true;
        return this;
    }

    public Stock updateDetails(String newName, BigDecimal newValue, Boolean isActive, ToolCategory newCategory){
        changeName(newName);
        changeValue(newValue);
        changeCategory(newCategory);
        this.isActive = isActive;
        this.updatedAt = new Date();

        return this;
    }

    private Stock changeCategory(ToolCategory newToolCategory) {
        if(Objects.isNull(newToolCategory)) {
            throw new IllegalArgumentException("Categoria não pode ser nula");
        }
        this.toolCategory = newToolCategory;
        this.updatedAt = new Date();

        return this;
    }

    private Stock changeName(String newName) {
        if(Objects.isNull(newName)) {
            throw new IllegalArgumentException("Nome do item não pode ser nulo.");
        }
        this.toolName = newName;
        this.updatedAt = new Date();

        return this;

    }

    private Stock changeValue(BigDecimal newValue) {
        if(Objects.isNull(newValue) || newValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Valor não pode ser nulo ou menor que zero.");
        }
        this.value = newValue;
        this.updatedAt = new Date();

        return this;
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
