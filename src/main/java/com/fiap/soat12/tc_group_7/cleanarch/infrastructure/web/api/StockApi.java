package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.dto.StockResponseDTO;
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

/**
 * Controlador dos endpoints para CRUD de itens de estoque.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/clean-arch/stock")
@Tag(name = "Estoque", description = "API para gerenciar itens em estoque")
public class StockApi {

    private final StockController stockController;

    @Operation(summary = "Cria um novo item de estoque",
            description = "Cria um novo item de estoque com base nos dados fornecidos, associando-o a uma categoria existente.")
    @ApiResponse(responseCode = "201", description = "Item de estoque criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockRequestDTO requestDTO) {
        try {
            StockResponseDTO createdStock = stockController.createStock(requestDTO);
            return new ResponseEntity<>(createdStock, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Obtém um item de estoque pelo ID",
            description = "Retorna um item de estoque específico pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Item de estoque encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @GetMapping("/{id}")
    public StockResponseDTO getStockById(@PathVariable UUID id) {
        return stockController.getStockById(id);
    }

    @Operation(summary = "Lista todos os itens de estoque",
            description = "Retorna uma lista de todos os itens em estoque cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque retornada com sucesso")
    @GetMapping("/all")
    public List<StockResponseDTO> getAllStockItems() {
        return stockController.getAllStockItems();
    }

    @Operation(summary = "Lista todos os itens de estoque ativos",
            description = "Retorna uma lista de todos os itens em estoque cadastrados e com status ativo")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque ativas retornada com sucesso")
    @GetMapping
    public List<StockResponseDTO> getAllStockItemsActive() {
        return stockController.getAllStockItemsActive();
    }

    @Operation(summary = "Atualiza um item de estoque existente",
            description = "Atualiza os dados de um item de estoque pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Item de estoque atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @PutMapping("/{id}")
    public StockResponseDTO updateStock(@PathVariable UUID id, @Valid @RequestBody StockRequestDTO requestDTO) {
        try {
            return stockController.updateStock(id, requestDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta um item de estoque",
            description = "Remove um item de estoque pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Item de estoque deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @DeleteMapping("/{id}")
    public void deleteStock(@PathVariable UUID id) {
        stockController.logicallyDeleteStock(id);
    }

    @Operation(summary = "Reativa um item de estoque",
            description = "Marca um item de estoque como ativo pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Item de estoque reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @PatchMapping("/{id}/reactivate")
    public StockResponseDTO reactivateStock(@PathVariable UUID id) {
        return stockController.reactivateStock(id);
    }
}
