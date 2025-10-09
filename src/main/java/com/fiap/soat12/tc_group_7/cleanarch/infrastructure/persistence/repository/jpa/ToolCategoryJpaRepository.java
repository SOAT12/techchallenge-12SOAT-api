package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ToolCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ToolCategoryJpaRepository extends JpaRepository<ToolCategoryEntity, UUID> {

    Optional<ToolCategoryEntity> findByToolCategoryName(String toolCategoryName);
    List<ToolCategoryEntity> findByActiveTrue();
}
