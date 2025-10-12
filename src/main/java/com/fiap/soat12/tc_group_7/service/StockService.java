package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderStockEntity;
import com.fiap.soat12.tc_group_7.dto.stock.StockAvailabilityResponseDTO;
import com.fiap.soat12.tc_group_7.dto.stock.StockRequestDTO;
import com.fiap.soat12.tc_group_7.dto.stock.StockResponseDTO;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Stock;
import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import com.fiap.soat12.tc_group_7.repository.StockRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço para operações de negócio relacionadas a Stock.
 */
@Service
public class StockService {

    private final StockRepository stockRepository;
    private final ToolCategoryService toolCategoryService;

    public StockService(StockRepository stockRepository, ToolCategoryService toolCategoryService) {
        this.stockRepository = stockRepository;
        this.toolCategoryService = toolCategoryService;
    }

    @Transactional
    public StockResponseDTO createStock(StockRequestDTO requestDTO) {
        ToolCategory toolCategory = toolCategoryService.getToolCategoryEntityById(requestDTO.getToolCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria da ferramenta não encontrada com ID: " + requestDTO.getToolCategoryId()));

        Stock stock = new Stock();
        BeanUtils.copyProperties(requestDTO, stock);
        stock.setToolCategory(toolCategory);

        Stock savedStock = stockRepository.save(stock);
        return convertToResponseDTO(savedStock);
    }

    @Transactional(readOnly = true)
    public Optional<StockResponseDTO> getStockById(Long id) {
        return stockRepository.findById(id)
                .filter(Stock::getActive)
                .map(this::convertToResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<StockResponseDTO> getAllStockItems() {
        return stockRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StockResponseDTO> getAllStockItemsActive() {
        return stockRepository.findByActiveTrue().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<StockResponseDTO> updateStock(Long id, StockRequestDTO requestDTO) {
        return stockRepository.findById(id)
                .map(existingStock -> {
                    ToolCategory toolCategory = toolCategoryService.getToolCategoryEntityById(requestDTO.getToolCategoryId())
                            .orElseThrow(() -> new IllegalArgumentException("Categoria da ferramenta não encontrada com ID: " + requestDTO.getToolCategoryId()));

                    BeanUtils.copyProperties(requestDTO, existingStock);
                    existingStock.setToolCategory(toolCategory);

                    Stock updatedStock = stockRepository.save(existingStock);
                    return convertToResponseDTO(updatedStock);
                });
    }

    @Transactional
    public boolean logicallyDeleteStock(Long id) {
        return stockRepository.findById(id)
                .map(stock -> {
                    stock.setActive(false);
                    stockRepository.save(stock);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public Optional<StockResponseDTO> reactivateStock(Long id) {
        return stockRepository.findById(id)
                .map(stock -> {
                    stock.setActive(true);
                    Stock reactivatedStock = stockRepository.save(stock);
                    return convertToResponseDTO(reactivatedStock);
                });
    }

    public StockAvailabilityResponseDTO checkStockAvailability(ServiceOrderEntity order) {
        List<StockAvailabilityResponseDTO.MissingItemDTO> missingItems = new ArrayList<>();

        for (ServiceOrderStockEntity requiredItem : order.getStockItems()) {

            Stock availableStock = stockRepository.findByIdAndActiveTrue(requiredItem.getStock().getId());

            int requiredQuantity = requiredItem.getStock().getQuantity();
            int availableQuantity = availableStock.getQuantity();

            if (availableQuantity < requiredQuantity) {
                missingItems.add(new StockAvailabilityResponseDTO.MissingItemDTO(
                        availableStock.getId(),
                        availableStock.getToolName(),
                        requiredQuantity,
                        availableQuantity
                ));
            }
        }

        boolean allItemsAvailable = missingItems.isEmpty();
        return new StockAvailabilityResponseDTO(allItemsAvailable, missingItems);
    }

    private StockResponseDTO convertToResponseDTO(Stock stock) {
        StockResponseDTO dto = new StockResponseDTO();
        BeanUtils.copyProperties(stock, dto);
        if (stock.getToolCategory() != null) {
            ToolCategoryResponseDTO categoryDTO = new ToolCategoryResponseDTO();
            BeanUtils.copyProperties(stock.getToolCategory(), categoryDTO);
            dto.setToolCategory(categoryDTO);
        }
        return dto;
    }
}
