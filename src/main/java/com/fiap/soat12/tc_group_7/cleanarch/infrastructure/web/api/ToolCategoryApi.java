package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ToolCategoryController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.ToolCategoryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/clean-arch/tool-categories")
@Tag(name = "Categorias de Ferramentas", description = "API para gerenciar categorias de ferramentas")
public class ToolCategoryApi {

    private final ToolCategoryController toolCategoryController;

    @Operation(summary = "Cria uma nova categoria de ferramenta",
            description = "Cria uma nova categoria de ferramenta com base nos dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @PostMapping
    public ResponseEntity<ToolCategoryResponseDTO> createToolCategory(@Valid @RequestBody ToolCategoryRequestDTO requestDTO) {
        try {
            ToolCategoryResponseDTO createdToolCategory = toolCategoryController.createToolCategory(requestDTO);
            return new ResponseEntity<>(createdToolCategory, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém uma categoria de ferramenta pelo ID",
            description = "Retorna uma categoria de ferramenta específica pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria encontrada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @GetMapping("/{id}")
    public ToolCategoryResponseDTO getToolCategoryById(@PathVariable UUID id) {
        return toolCategoryController.getToolCategoryById(id);
    }

    @Operation(summary = "Lista todas as categorias de ferramentas",
            description = "Retorna uma lista de todas as categorias de ferramentas cadastradas.")
    @ApiResponse(responseCode = "200", description = "Lista de categorias retornada com sucesso")
    @GetMapping("/all")
    public List<ToolCategoryResponseDTO> getAllToolCategories() {
        return toolCategoryController.getAllToolCategories();
    }

    @Operation(summary = "Lista todas as categorias de ferramentas ativas",
            description = "Retorna uma lista de todas as categorias de ferramentas cadastradas e com status ativo")
    @ApiResponse(responseCode = "200", description = "Lista de categorias ativas retornada com sucesso")
    @GetMapping
    public List<ToolCategoryResponseDTO> getAllToolCategoriesActive() {
        return toolCategoryController.getAllToolCategoriesActive();
    }

    @Operation(summary = "Atualiza uma categoria de ferramenta existente",
            description = "Atualiza os dados de uma categoria de ferramenta pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PutMapping("/{id}")
    public ToolCategoryResponseDTO updateToolCategory(@PathVariable UUID id, @Valid @RequestBody ToolCategoryRequestDTO requestDTO) {
        try {
            return toolCategoryController.updateToolCategory(id, requestDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta uma categoria de ferramenta",
            description = "Remove uma categoria de ferramenta pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @DeleteMapping("/{id}")
    public void deleteToolCategory(@PathVariable UUID id) {
        toolCategoryController.logicallyDeleteToolCategory(id);
    }

    @Operation(summary = "Reativa uma categoria de ferramenta",
            description = "Marca uma categoria de ferramenta como ativa pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Categoria reativada com sucesso")
    @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    @PatchMapping("/{id}/reactivate")
    public ToolCategoryResponseDTO reactivateToolCategory(@PathVariable UUID id) {
        return toolCategoryController.reactivateToolCategory(id);
    }
}
