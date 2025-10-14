package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ToolCategoryController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToolCategoryApiTest {

    @Mock
    private ToolCategoryController toolCategoryController;

    private ToolCategoryApi toolCategoryApi;

    private UUID sampleUuid = UUID.randomUUID();

    @BeforeEach
    void setup() {
        toolCategoryApi = new ToolCategoryApi(toolCategoryController);
    }

    @Test
    void createToolCategory_ShouldReturnCreatedCategory() {
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO();
        // preencha campos obrigatórios do requestDTO

        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();
        // preencha campos do responseDTO, por exemplo:
        // responseDTO.setId(sampleUuid);

        when(toolCategoryController.createToolCategory(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<ToolCategoryResponseDTO> response = toolCategoryApi.createToolCategory(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
        verify(toolCategoryController).createToolCategory(requestDTO);
    }

    @Test
    void createToolCategory_ShouldThrowBadRequest_WhenInvalidData() {
        ToolCategoryRequestDTO invalidRequest = new ToolCategoryRequestDTO();

        when(toolCategoryController.createToolCategory(invalidRequest))
                .thenThrow(new IllegalArgumentException("Requisição inválida"));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            toolCategoryApi.createToolCategory(invalidRequest);
        });

        assertEquals("Requisição inválida", thrown.getReason());
        verify(toolCategoryController).createToolCategory(invalidRequest);
    }

    @Test
    void getToolCategoryById_ShouldReturnCategory() {
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();
        // responseDTO.setId(sampleUuid);

        when(toolCategoryController.getToolCategoryById(sampleUuid)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryApi.getToolCategoryById(sampleUuid);

        assertEquals(responseDTO, result);
        verify(toolCategoryController).getToolCategoryById(sampleUuid);
    }

    @Test
    void getAllToolCategories_ShouldReturnList() {
        List<ToolCategoryResponseDTO> list = List.of(
                new ToolCategoryResponseDTO(),
                new ToolCategoryResponseDTO()
        );

        when(toolCategoryController.getAllToolCategories()).thenReturn(list);

        List<ToolCategoryResponseDTO> result = toolCategoryApi.getAllToolCategories();

        assertEquals(2, result.size());
        verify(toolCategoryController).getAllToolCategories();
    }

    @Test
    void getAllToolCategoriesActive_ShouldReturnList() {
        List<ToolCategoryResponseDTO> list = List.of(
                new ToolCategoryResponseDTO(),
                new ToolCategoryResponseDTO()
        );

        when(toolCategoryController.getAllToolCategoriesActive()).thenReturn(list);

        List<ToolCategoryResponseDTO> result = toolCategoryApi.getAllToolCategoriesActive();

        assertEquals(2, result.size());
        verify(toolCategoryController).getAllToolCategoriesActive();
    }

    @Test
    void updateToolCategory_ShouldReturnUpdatedCategory() {
        ToolCategoryRequestDTO requestDTO = new ToolCategoryRequestDTO();

        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryController.updateToolCategory(sampleUuid, requestDTO)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryApi.updateToolCategory(sampleUuid, requestDTO);

        assertEquals(responseDTO, result);
        verify(toolCategoryController).updateToolCategory(sampleUuid, requestDTO);
    }

    @Test
    void updateToolCategory_ShouldThrowBadRequest_WhenInvalidData() {
        ToolCategoryRequestDTO invalidRequest = new ToolCategoryRequestDTO();

        when(toolCategoryController.updateToolCategory(sampleUuid, invalidRequest))
                .thenThrow(new IllegalArgumentException("Requisição inválida"));

        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            toolCategoryApi.updateToolCategory(sampleUuid, invalidRequest);
        });

        assertEquals("Requisição inválida", thrown.getReason());
        verify(toolCategoryController).updateToolCategory(sampleUuid, invalidRequest);
    }

    @Test
    void deleteToolCategory_ShouldCallDelete() {
        doNothing().when(toolCategoryController).logicallyDeleteToolCategory(sampleUuid);

        toolCategoryApi.deleteToolCategory(sampleUuid);

        verify(toolCategoryController).logicallyDeleteToolCategory(sampleUuid);
    }

    @Test
    void reactivateToolCategory_ShouldReturnCategory() {
        ToolCategoryResponseDTO responseDTO = new ToolCategoryResponseDTO();

        when(toolCategoryController.reactivateToolCategory(sampleUuid)).thenReturn(responseDTO);

        ToolCategoryResponseDTO result = toolCategoryApi.reactivateToolCategory(sampleUuid);

        assertEquals(responseDTO, result);
        verify(toolCategoryController).reactivateToolCategory(sampleUuid);
    }

}
