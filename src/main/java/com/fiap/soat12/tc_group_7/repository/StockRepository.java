package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.Stock;
import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    List<Stock> findByToolCategory(ToolCategory toolCategory);
    List<Stock> findByActiveTrueAndQuantityGreaterThan(Integer quantity);
    List<Stock> findByActiveTrue();
    Stock findByIdAndActiveTrue(Long id);
}
