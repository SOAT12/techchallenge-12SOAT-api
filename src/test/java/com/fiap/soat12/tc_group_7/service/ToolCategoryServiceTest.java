package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import com.fiap.soat12.tc_group_7.repository.ToolCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Classe de testes unitários para ToolCategoryService.
 * Utiliza Mockito para simular o comportamento do ToolCategoryRepository.
 */
@ExtendWith(MockitoExtension.class)
class ToolCategoryServiceTest {

    @Mock
    private ToolCategoryRepository toolCategoryRepository;

    @InjectMocks
    private ToolCategoryService toolCategoryService;

    private ToolCategory activeCategory;
    private ToolCategory inactiveCategory;
    private ToolCategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        activeCategory = new ToolCategory(1L, "Pecas", true);
        inactiveCategory = new ToolCategory(2L, "Insumos", false);
        requestDTO = new ToolCategoryRequestDTO("Ferramentas Manuais", true);
    }

    @Test
    @DisplayName("Deve criar uma nova categoria de ferramenta com sucesso")
    void shouldCreateToolCategorySuccessfully() {
        when(toolCategoryRepository.save(any(ToolCategory.class))).thenReturn(activeCategory);

        ToolCategoryResponseDTO result = toolCategoryService.createToolCategory(requestDTO);

        assertNotNull(result);
        assertEquals(activeCategory.getId(), result.getId());
        assertEquals(activeCategory.getToolCategoryName(), result.getToolCategoryName());
        assertEquals(activeCategory.getActive(), result.getActive());

        verify(toolCategoryRepository, times(1)).save(any(ToolCategory.class));
    }

    @Test
    @DisplayName("Deve retornar uma categoria ativa pelo ID")
    void shouldReturnActiveToolCategoryById() {
        when(toolCategoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.getToolCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals(activeCategory.getId(), result.get().getId());
        assertEquals(activeCategory.getToolCategoryName(), result.get().getToolCategoryName());
        assertEquals(activeCategory.getActive(), result.get().getActive());

        verify(toolCategoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Não deve retornar uma categoria inativa pelo ID")
    void shouldNotReturnInactiveToolCategoryById() {
        when(toolCategoryRepository.findById(2L)).thenReturn(Optional.of(inactiveCategory));

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.getToolCategoryById(2L);

        assertFalse(result.isPresent());

        verify(toolCategoryRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("Deve retornar Optional.empty se a categoria não for encontrada pelo ID")
    void shouldReturnEmptyOptionalWhenToolCategoryNotFoundById() {
        when(toolCategoryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.getToolCategoryById(99L);

        assertFalse(result.isPresent());

        verify(toolCategoryRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve listar todas as categorias")
    void shouldListAllToolCategories() {
        when(toolCategoryRepository.findAll()).thenReturn(Arrays.asList(activeCategory));

        List<ToolCategoryResponseDTO> result = toolCategoryService.getAllToolCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activeCategory.getToolCategoryName(), result.getFirst().getToolCategoryName());
        assertTrue(result.getFirst().getActive());

        verify(toolCategoryRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve listar todas as categorias ativas")
    void shouldListAllActiveToolCategories() {
        when(toolCategoryRepository.findByActiveTrue()).thenReturn(Arrays.asList(activeCategory));

        List<ToolCategoryResponseDTO> result = toolCategoryService.getAllToolCategoriesActive();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(activeCategory.getToolCategoryName(), result.getFirst().getToolCategoryName());
        assertTrue(result.getFirst().getActive());

        verify(toolCategoryRepository, times(1)).findByActiveTrue();
    }

    @Test
    @DisplayName("Deve atualizar uma categoria de ferramenta existente com sucesso")
    void shouldUpdateToolCategorySuccessfully() {
        ToolCategoryRequestDTO updateRequest = new ToolCategoryRequestDTO("Ferramentas Elétricas", false);
        ToolCategory updatedCategory = new ToolCategory(1L, "Ferramentas Elétricas", false);

        when(toolCategoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(toolCategoryRepository.save(any(ToolCategory.class))).thenReturn(updatedCategory);

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.updateToolCategory(1L, updateRequest);

        assertTrue(result.isPresent());
        assertEquals(updatedCategory.getId(), result.get().getId());
        assertEquals(updatedCategory.getToolCategoryName(), result.get().getToolCategoryName());
        assertEquals(updatedCategory.getActive(), result.get().getActive());

        verify(toolCategoryRepository, times(1)).findById(1L);
        verify(toolCategoryRepository, times(1)).save(any(ToolCategory.class));
    }

    @Test
    @DisplayName("Não deve atualizar categoria se não for encontrada")
    void shouldNotUpdateToolCategoryIfNotFound() {
        when(toolCategoryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.updateToolCategory(99L, requestDTO);

        assertFalse(result.isPresent());

        verify(toolCategoryRepository, times(1)).findById(99L);
        verify(toolCategoryRepository, never()).save(any(ToolCategory.class));
    }

    @Test
    @DisplayName("Deve deletar logicamente uma categoria com sucesso")
    void shouldLogicallyDeleteToolCategorySuccessfully() {
        when(toolCategoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));
        when(toolCategoryRepository.save(any(ToolCategory.class))).thenReturn(activeCategory);

        boolean result = toolCategoryService.logicallyDeleteToolCategory(1L);

        assertTrue(result);
        assertFalse(activeCategory.getActive());

        verify(toolCategoryRepository, times(1)).findById(1L);
        verify(toolCategoryRepository, times(1)).save(activeCategory);
    }

    @Test
    @DisplayName("Não deve deletar logicamente uma categoria se não for encontrada")
    void shouldNotLogicallyDeleteToolCategoryIfNotFound() {
        when(toolCategoryRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = toolCategoryService.logicallyDeleteToolCategory(99L);

        assertFalse(result);

        verify(toolCategoryRepository, times(1)).findById(99L);
        verify(toolCategoryRepository, never()).save(any(ToolCategory.class));
    }

    @Test
    @DisplayName("Deve reativar uma categoria de ferramenta com sucesso")
    void shouldReactivateToolCategorySuccessfully() {
        when(toolCategoryRepository.findById(2L)).thenReturn(Optional.of(inactiveCategory));
        when(toolCategoryRepository.save(any(ToolCategory.class))).thenReturn(inactiveCategory);

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.reactivateToolCategory(2L);

        assertTrue(result.isPresent());
        assertTrue(inactiveCategory.getActive());

        verify(toolCategoryRepository, times(1)).findById(2L);
        verify(toolCategoryRepository, times(1)).save(inactiveCategory);
    }

    @Test
    @DisplayName("Não deve reativar categoria se não for encontrada")
    void shouldNotReactivateToolCategoryIfNotFound() {
        when(toolCategoryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ToolCategoryResponseDTO> result = toolCategoryService.reactivateToolCategory(99L);

        assertFalse(result.isPresent());

        verify(toolCategoryRepository, times(1)).findById(99L);
        verify(toolCategoryRepository, never()).save(any(ToolCategory.class));
    }

    @Test
    @DisplayName("Deve retornar a entidade ToolCategory ativa pelo ID")
    void shouldReturnActiveToolCategoryEntityById() {
        when(toolCategoryRepository.findById(1L)).thenReturn(Optional.of(activeCategory));

        Optional<ToolCategory> result = toolCategoryService.getToolCategoryEntityById(1L);

        assertTrue(result.isPresent());
        assertEquals(activeCategory, result.get());

        verify(toolCategoryRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Não deve retornar a entidade ToolCategory inativa pelo ID")
    void shouldNotReturnInactiveToolCategoryEntityById() {
        when(toolCategoryRepository.findById(2L)).thenReturn(Optional.of(inactiveCategory));

        Optional<ToolCategory> result = toolCategoryService.getToolCategoryEntityById(2L);

        assertFalse(result.isPresent());

        verify(toolCategoryRepository, times(1)).findById(2L);
    }
}
