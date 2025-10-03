package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringStockRepository extends JpaRepository<StockEntity, UUID> {
    Optional<StockEntity> findByIdAndActiveTrue(UUID id);

    List<StockEntity> findByActiveTrue();

    boolean existsByToolName(String name);

    Optional<StockEntity> findByToolName(String name);
}
