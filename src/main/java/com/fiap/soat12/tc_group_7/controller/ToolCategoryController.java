package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ToolCategoryResponseDTO;
import com.fiap.soat12.tc_group_7.service.ToolCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador dos endpoints para CRUD de categorias de ferramentas.
 */
@RestController
@RequestMapping("/api/tool-categories")
@Tag(name = "Categorias de Ferramentas", description = "API para gerenciar categorias de ferramentas")
public class ToolCategoryController {

    private final ToolCategoryService toolCategoryService;

    public ToolCategoryController(ToolCategoryService toolCategoryService) {
        this.toolCategoryService = toolCategoryService;
    }

    @Operation(summary = "Cria uma nova categoria de ferramenta",
            description = "Cria uma nova categoria de ferramenta com base nos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @PostMapping
    public ResponseEntity<ToolCategoryResponseDTO> createToolCategory(@Valid @RequestBody ToolCategoryRequestDTO requestDTO) {
        ToolCategoryResponseDTO createdCategory = toolCategoryService.createToolCategory(requestDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtém uma categoria de ferramenta pelo ID",
            description = "Retorna uma categoria de ferramenta específica pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @GetMapping("/{id}")
    public ResponseEntity<ToolCategoryResponseDTO> getToolCategoryById(@PathVariable Long id) {
        return toolCategoryService.getToolCategoryById(id)
                .map(category -> new ResponseEntity<>(category, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lista todas as categorias de ferramentas",
            description = "Retorna uma lista de todas as categorias de ferramentas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<ToolCategoryResponseDTO>> getAllToolCategories() {
        List<ToolCategoryResponseDTO> categories = toolCategoryService.getAllToolCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Lista todas as categorias de ferramentas ativas",
            description = "Retorna uma lista de todas as categorias de ferramentas cadastradas e com status ativo")
    @ApiResponse(responseCode = "200", description = "Lista de categorias ativas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<ToolCategoryResponseDTO>> getAllToolCategoriesActive() {
        List<ToolCategoryResponseDTO> categories = toolCategoryService.getAllToolCategoriesActive();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza uma categoria de ferramenta existente",
            description = "Atualiza os dados de uma categoria de ferramenta pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PutMapping("/{id}")
    public ResponseEntity<ToolCategoryResponseDTO> updateToolCategory(@PathVariable Long id, @Valid @RequestBody ToolCategoryRequestDTO requestDTO) {
        return toolCategoryService.updateToolCategory(id, requestDTO)
                .map(updatedCategory -> new ResponseEntity<>(updatedCategory, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Deleta uma categoria de ferramenta",
            description = "Remove uma categoria de ferramenta pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteToolCategory(@PathVariable Long id) {
        if (toolCategoryService.logicallyDeleteToolCategory(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reativa uma categoria de ferramenta",
            description = "Marca uma categoria de ferramenta como ativa pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria reativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<ToolCategoryResponseDTO> reactivateToolCategory(@PathVariable Long id) {
        return toolCategoryService.reactivateToolCategory(id)
                .map(reactivatedCategory -> new ResponseEntity<>(reactivatedCategory, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
