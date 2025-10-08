package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ToolCategory;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.ToolCategoryMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
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
        return Optional.empty();
    }

    /**
     * @param toolCategoryName
     * @return
     */
    @Override
    public Optional<ToolCategory> findByToolCategoryName(String toolCategoryName) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<ToolCategory> findAllActive() {
        return List.of();
    }

    /**
     * @return
     */
    @Override
    public List<ToolCategory> findAll() {
        return List.of();
    }

    /**
     * @param newToolCategory
     * @return
     */
    @Override
    public ToolCategory save(ToolCategory newToolCategory) {
        return null;
    }
}
