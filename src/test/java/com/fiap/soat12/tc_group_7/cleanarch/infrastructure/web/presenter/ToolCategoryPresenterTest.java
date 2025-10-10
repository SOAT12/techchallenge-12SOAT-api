package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ToolCategoryPresenterTest {

    @Test
    void toToolCategoryResponseDTO_ShouldConvertCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "Wrenches";
        ToolCategory toolCategory = new ToolCategory(id, name, true);

        ToolCategoryPresenter presenter = new ToolCategoryPresenter();
        ToolCategoryResponseDTO dto = presenter.toToolCategoryResponseDTO(toolCategory);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(name, dto.getToolCategoryName());
        assertTrue(dto.getActive());
    }
}
