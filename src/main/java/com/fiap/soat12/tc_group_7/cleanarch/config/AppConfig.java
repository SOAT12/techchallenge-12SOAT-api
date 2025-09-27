package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.controller.VehicleServiceController;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleDataSource;
import com.fiap.soat12.tc_group_7.cleanarch.repository.JdbcVehicleServiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class AppConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }

    @Bean
    public VehicleDataSource vehicleDataSource(DataSource dataSource) {
        return new JdbcVehicleServiceRepository(dataSource);
    }

    @Bean
    public VehicleServiceController vehicleServiceController(VehicleDataSource vehicleDataSource) {
        return new VehicleServiceController(vehicleDataSource);
    }

}
