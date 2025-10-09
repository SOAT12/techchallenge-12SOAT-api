package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.StockRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.ToolCategoryRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ToolCategoryController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ToolCategoryRepository toolCategoryDataSource(ToolCategoryJpaRepository toolCategoryJpaRepository) {
        return new ToolCategoryRepositoryImpl(toolCategoryJpaRepository);
    }

    @Bean
    public ToolCategoryController toolCategoryController(ToolCategoryRepository toolCategoryRepository) {
        return new ToolCategoryController(toolCategoryRepository);
    }

    @Bean
    public StockRepository stockDataSource(StockJpaRepository stockJpaRepository) {
        return new StockRepositoryImpl(stockJpaRepository);
    }

    @Bean
    public StockController stockController(StockRepository stockRepository, ToolCategoryRepository toolCategoryRepository) {
        return new StockController(stockRepository, toolCategoryRepository);
    }
}
