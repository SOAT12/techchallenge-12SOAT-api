package com.fiap.soat12.tc_group_7.controller;

import com.fiap.soat12.tc_group_7.dto.StockRequestDTO;
import com.fiap.soat12.tc_group_7.dto.StockResponseDTO;
import com.fiap.soat12.tc_group_7.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Controlador dos endpoints para CRUD de itens de estoque.
 */
@RestController
@RequestMapping("/api/stock")
@Tag(name = "Estoque", description = "API para gerenciar itens em estoque")
public class StockController {

    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @Operation(summary = "Cria um novo item de estoque",
            description = "Cria um novo item de estoque com base nos dados fornecidos, associando-o a uma categoria existente.")
    @ApiResponse(responseCode = "201", description = "Item de estoque criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @PostMapping
    public ResponseEntity<StockResponseDTO> createStock(@Valid @RequestBody StockRequestDTO requestDTO) {
        try {
            StockResponseDTO createdStock = stockService.createStock(requestDTO);
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
    public ResponseEntity<StockResponseDTO> getStockById(@PathVariable Long id) {
        return stockService.getStockById(id)
                .map(stock -> new ResponseEntity<>(stock, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Lista todos os itens de estoque",
            description = "Retorna uma lista de todos os itens em estoque cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque retornada com sucesso")
    @GetMapping("/all")
    public ResponseEntity<List<StockResponseDTO>> getAllStockItems() {
        List<StockResponseDTO> stockItems = stockService.getAllStockItems();
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @Operation(summary = "Lista todos os itens de estoque ativos",
            description = "Retorna uma lista de todos os itens em estoque cadastrados e com status ativo")
    @ApiResponse(responseCode = "200", description = "Lista de itens de estoque ativas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<StockResponseDTO>> getAllStockItemsActive() {
        List<StockResponseDTO> stockItems = stockService.getAllStockItemsActive();
        return new ResponseEntity<>(stockItems, HttpStatus.OK);
    }

    @Operation(summary = "Atualiza um item de estoque existente",
            description = "Atualiza os dados de um item de estoque pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Item de estoque atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida ou categoria não encontrada")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @PutMapping("/{id}")
    public ResponseEntity<StockResponseDTO> updateStock(@PathVariable Long id, @Valid @RequestBody StockRequestDTO requestDTO) {
        try {
            return stockService.updateStock(id, requestDTO)
                    .map(updatedStock -> new ResponseEntity<>(updatedStock, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(summary = "Deleta um item de estoque",
            description = "Remove um item de estoque pelo seu ID.")
    @ApiResponse(responseCode = "204", description = "Item de estoque deletado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStock(@PathVariable Long id) {
        if (stockService.logicallyDeleteStock(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Reativa um item de estoque",
            description = "Marca um item de estoque como ativo pelo seu ID.")
    @ApiResponse(responseCode = "200", description = "Item de estoque reativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item de estoque não encontrado")
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<StockResponseDTO> reactivateStock(@PathVariable Long id) {
        return stockService.reactivateStock(id)
                .map(reactivatedStock -> new ResponseEntity<>(reactivatedStock, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
