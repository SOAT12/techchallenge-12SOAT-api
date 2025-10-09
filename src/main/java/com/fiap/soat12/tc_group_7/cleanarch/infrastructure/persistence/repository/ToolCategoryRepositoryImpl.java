package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.ToolCategoryMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ToolCategoryRepositoryImpl implements ToolCategoryRepository {

    private final ToolCategoryJpaRepository toolCategoryJpaRepository;
    private final ToolCategoryMapper toolCategoryMapper;

    public ToolCategoryRepositoryImpl(ToolCategoryJpaRepository toolCategoryJpaRepository) {
        this.toolCategoryJpaRepository = toolCategoryJpaRepository;
        this.toolCategoryMapper = new ToolCategoryMapper();
    }

    /**
     * @param toolCategoryId
     * @return
     */
    @Override
    public Optional<ToolCategory> findById(UUID toolCategoryId) {
        return toolCategoryJpaRepository.findById(toolCategoryId)
                .map(toolCategoryMapper::toDomain);
    }

    /**
     * @param toolCategoryName
     * @return
     */
    @Override
    public Optional<ToolCategory> findByToolCategoryName(String toolCategoryName) {
        return toolCategoryJpaRepository.findByToolCategoryName(toolCategoryName)
                .map(toolCategoryMapper::toDomain);
    }

    /**
     * @return
     */
    @Override
    public List<ToolCategory> findAllActive() {
        return toolCategoryJpaRepository.findByActiveTrue().stream()
                .map(toolCategoryMapper::toDomain).toList();
    }

    /**
     * @return
     */
    @Override
    public List<ToolCategory> findAll() {
        return toolCategoryJpaRepository.findAll().stream()
                .map(toolCategoryMapper::toDomain).toList();
    }

    /**
     * @param newToolCategory
     * @return
     */
    @Override
    public ToolCategory save(ToolCategory newToolCategory) {
        ToolCategoryEntity entity = toolCategoryMapper.toEntity(newToolCategory);
        ToolCategoryEntity savedCategory = toolCategoryJpaRepository.save(entity);

        return toolCategoryMapper.toDomain(savedCategory);
    }
}
