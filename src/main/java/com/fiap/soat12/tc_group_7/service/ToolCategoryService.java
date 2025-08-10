package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryRequestDTO;
import com.fiap.soat12.tc_group_7.dto.toolCategory.ToolCategoryResponseDTO;
import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import com.fiap.soat12.tc_group_7.repository.ToolCategoryRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço para operações de negócio relacionadas a ToolCategory.
 */
@Service
public class ToolCategoryService {

    private final ToolCategoryRepository toolCategoryRepository;

    public ToolCategoryService(ToolCategoryRepository toolCategoryRepository) {
        this.toolCategoryRepository = toolCategoryRepository;
    }

    @Transactional
    public ToolCategoryResponseDTO createToolCategory(ToolCategoryRequestDTO requestDTO) {
        ToolCategory toolCategory = new ToolCategory();
        BeanUtils.copyProperties(requestDTO, toolCategory);
        ToolCategory savedCategory = toolCategoryRepository.save(toolCategory);
        return convertToResponseDTO(savedCategory);
    }

    @Transactional(readOnly = true)
    public Optional<ToolCategoryResponseDTO> getToolCategoryById(Long id) {
        return toolCategoryRepository.findById(id)
                .filter(ToolCategory::getActive)
                .map(this::convertToResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<ToolCategoryResponseDTO> getAllToolCategories() {
        return toolCategoryRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ToolCategoryResponseDTO> getAllToolCategoriesActive() {
        return toolCategoryRepository.findByActiveTrue().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public Optional<ToolCategoryResponseDTO> updateToolCategory(Long id, ToolCategoryRequestDTO requestDTO) {
        return toolCategoryRepository.findById(id)
                .map(existingCategory -> {
                    BeanUtils.copyProperties(requestDTO, existingCategory);
                    ToolCategory updatedCategory = toolCategoryRepository.save(existingCategory);
                    return convertToResponseDTO(updatedCategory);
                });
    }

    @Transactional
    public boolean logicallyDeleteToolCategory(Long id) {
        return toolCategoryRepository.findById(id)
                .map(toolCategory -> {
                    toolCategory.setActive(false);
                    toolCategoryRepository.save(toolCategory);
                    return true;
                }).orElse(false);
    }

    @Transactional
    public Optional<ToolCategoryResponseDTO> reactivateToolCategory(Long id) {
        return toolCategoryRepository.findById(id)
                .map(toolCategory -> {
                    toolCategory.setActive(true);
                    ToolCategory reactivatedCategory = toolCategoryRepository.save(toolCategory);
                    return convertToResponseDTO(reactivatedCategory);
                });
    }

    private ToolCategoryResponseDTO convertToResponseDTO(ToolCategory toolCategory) {
        ToolCategoryResponseDTO dto = new ToolCategoryResponseDTO();
        BeanUtils.copyProperties(toolCategory, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public Optional<ToolCategory> getToolCategoryEntityById(Long id) {
        return toolCategoryRepository.findById(id).filter(ToolCategory::getActive);
    }
}
