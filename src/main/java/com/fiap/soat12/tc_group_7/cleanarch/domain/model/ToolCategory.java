package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;

import java.util.Objects;
import java.util.UUID;

public class ToolCategory {

    private final UUID id;
    private String toolCategoryName;
    private Boolean isActive;

    /* CONSTRUCTORS */
    public ToolCategory(String toolCategoryName) {
        if(toolCategoryName == null || toolCategoryName.isBlank()) {
            throw new IllegalArgumentException("A tool category name must not be null or blank");
        }

        this.id = UUID.randomUUID();
        this.toolCategoryName = toolCategoryName;
        this.isActive = true;
    }

    public ToolCategory(UUID id, String toolCategoryName, Boolean isActive) {
        this.id = id;
        this.toolCategoryName = toolCategoryName;
        this.isActive = isActive;
    }

    /* BUSINESS LOGIC METHODS */
    public ToolCategory changeName(String newToolCategoryName) {
        if(Objects.isNull(newToolCategoryName)) {
            throw new IllegalArgumentException("Nome da categoria não pode ser nulo.");
        }
        this.toolCategoryName = newToolCategoryName;
        return this;
    }

    public ToolCategory deactivate() {
        if (Boolean.FALSE.equals(this.isActive)) {
            throw new IllegalStateException("Categoria já encontra-se desativada.");
        }
        this.isActive = false;
        return this;
    }

    public ToolCategory activate() {
        if (Boolean.TRUE.equals(this.isActive)) {
            throw new IllegalArgumentException("Categoria já encontra-se ativada.");
        }
        this.isActive = true;
        return this;
    }

    /* GETTERS AND SETTERS */
    public UUID getId() {return id;}

    public String getToolCategoryName() {return toolCategoryName;}

    public Boolean getActive() {return isActive;}

    // --- DEFAULT OBJECT METHODS (equals, hashCode, toString) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ToolCategory toolCategory = (ToolCategory) o;
        return Objects.equals(id, toolCategory.id); // Equality is based on the unique ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Stock {" +
                "id=" + id +
                ", toolCategoryName='" + toolCategoryName + '\'' +
                ", isActive=" + isActive + '\'' +
                '}';
    }
}
