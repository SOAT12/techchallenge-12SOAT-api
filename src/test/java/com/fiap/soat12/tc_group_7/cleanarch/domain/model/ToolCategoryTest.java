package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class ToolCategoryTest {

    @Test
    void create_WithValidName_ShouldReturnToolCategory() {
        String categoryName = "Hammers";
        ToolCategory category = ToolCategory.create(categoryName);

        assertNotNull(category);
        assertEquals(categoryName, category.getToolCategoryName());
        assertTrue(category.getActive());
    }

    @Test
    void create_WithNullName_ShouldThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ToolCategory.create(null);
        });
        assertEquals("A tool category name must not be null or blank", exception.getMessage());
    }

    @Test
    void create_WithBlankName_ShouldThrowIllegalArgumentException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ToolCategory.create(" ");
        });
        assertEquals("A tool category name must not be null or blank", exception.getMessage());
    }

    @Test
    void changeName_WithValidName_ShouldUpdateName() {
        ToolCategory category = ToolCategory.create("Old Name");
        String newName = "New Name";
        category.changeName(newName);
        assertEquals(newName, category.getToolCategoryName());
    }

    @Test
    void changeName_WithNullName_ShouldThrowIllegalArgumentException() {
        ToolCategory category = ToolCategory.create("Some Name");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            category.changeName(null);
        });
        assertEquals("Nome da categoria não pode ser nulo.", exception.getMessage());
    }

    @Test
    void deactivate_WhenActive_ShouldBecomeInactive() {
        ToolCategory category = ToolCategory.create("Actives");
        assertTrue(category.getActive());

        category.deactivate();
        assertFalse(category.getActive());
    }

    @Test
    void deactivate_WhenAlreadyInactive_ShouldThrowIllegalStateException() {
        ToolCategory category = ToolCategory.create("Inactive Test");
        category.deactivate(); // Deactivate once
        assertFalse(category.getActive());

        Exception exception = assertThrows(IllegalStateException.class, category::deactivate);
        assertEquals("Categoria já encontra-se desativada.", exception.getMessage());
    }

    @Test
    void activate_WhenInactive_ShouldBecomeActive() {
        ToolCategory category = ToolCategory.create("Reactivate Test");
        category.deactivate();
        assertFalse(category.getActive());

        category.activate();
        assertTrue(category.getActive());
    }

    @Test
    void activate_WhenAlreadyActive_ShouldThrowIllegalArgumentException() {
        ToolCategory category = ToolCategory.create("Active Test");
        assertTrue(category.getActive());

        Exception exception = assertThrows(IllegalArgumentException.class, category::activate);
        assertEquals("Categoria já encontra-se ativada.", exception.getMessage());
    }

    @Test
    void equals_and_hashCode_Contract() {
        UUID id = UUID.randomUUID();
        ToolCategory category1 = new ToolCategory(id, "Test", true);
        ToolCategory category2 = new ToolCategory(id, "Test", true);
        ToolCategory category3 = new ToolCategory(UUID.randomUUID(), "Test", true);

        // equals
        assertEquals(category1, category2);
        assertNotEquals(category1, category3);
        assertNotEquals(category1, null);
        assertNotEquals(category1, new Object());


        // hashCode
        assertEquals(category1.hashCode(), category2.hashCode());
        assertNotEquals(category1.hashCode(), category3.hashCode());
    }

    @Test
    void toString_ShouldContainClassInformation() {
        ToolCategory category = ToolCategory.create("Wrenches");
        String str = category.toString();

        assertTrue(str.contains("toolCategoryName='Wrenches'"));
        assertTrue(str.contains("isActive=true"));
    }
}
