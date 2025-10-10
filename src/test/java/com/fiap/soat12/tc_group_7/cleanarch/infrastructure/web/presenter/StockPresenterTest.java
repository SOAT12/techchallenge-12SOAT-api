package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Stock;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockPresenterTest {

    @Mock
    private ToolCategoryPresenter toolCategoryPresenter;

    @InjectMocks
    private StockPresenter stockPresenter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toStockResponseDTO_ShouldConvertCorrectly() {
        UUID categoryId = UUID.randomUUID();
        ToolCategory toolCategory = new ToolCategory(categoryId, "Hammers", true);

        UUID stockId = UUID.randomUUID();
        Date now = new Date();
        Stock stock = new Stock(stockId, "Sledgehammer", BigDecimal.TEN, 50, toolCategory, true, now, now);

        ToolCategoryResponseDTO categoryDTO = ToolCategoryResponseDTO.builder()
                .id(categoryId)
                .toolCategoryName("Hammers")
                .active(true)
                .build();

        when(toolCategoryPresenter.toToolCategoryResponseDTO(toolCategory)).thenReturn(categoryDTO);

        StockResponseDTO stockDTO = stockPresenter.toStockResponseDTO(stock);

        assertNotNull(stockDTO);
        assertEquals(stockId, stockDTO.getId());
        assertEquals("Sledgehammer", stockDTO.getToolName());
        assertEquals(BigDecimal.TEN, stockDTO.getValue());
        assertEquals(50, stockDTO.getQuantity());
        assertTrue(stockDTO.getIsActive());
        assertEquals(now, stockDTO.getCreated_at());
        assertEquals(now, stockDTO.getUpdated_at());
        assertNotNull(stockDTO.getToolCategory());
        assertEquals(categoryId, stockDTO.getToolCategory().getId());

        verify(toolCategoryPresenter, times(1)).toToolCategoryResponseDTO(toolCategory);
    }
}
