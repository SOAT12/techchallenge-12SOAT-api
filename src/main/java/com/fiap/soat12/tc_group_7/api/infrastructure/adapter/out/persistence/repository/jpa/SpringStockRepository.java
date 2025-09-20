package com.fiap.soat12.tc_group_7.api.infrastructure.adapter.out.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.api.infrastructure.adapter.out.persistence.entity.StockEntity;
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
