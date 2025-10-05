package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.StockMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.StockRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public StockRepository stockDataSource(StockJpaRepository stockJpaRepository, StockMapper stockMapper) {
        return new StockRepositoryImpl(stockJpaRepository, stockMapper);
    }

    @Bean
    public StockController stockController(StockRepository stockRepository) {
        return new StockController(stockRepository);
    }
}
