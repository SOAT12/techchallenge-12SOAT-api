package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.controller.*;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.StockGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.ToolCategoryGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.StockRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.ToolCategoryRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.StockJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ToolCategoryJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction.EmployeeFunctionJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction.EmployeeFunctionRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.employee.EmployeeJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.employee.EmployeeRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ToolCategoryController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public VehicleServiceRepository vehicleServiceDataSource(VehicleServiceJpaRepository vehicleServiceJpaRepository) {
        return new VehicleServiceRepositoryImpl(vehicleServiceJpaRepository);
    }

    @Bean
    public VehicleServiceController vehicleServiceController(VehicleServiceRepository vehicleServiceRepository) {
        return new VehicleServiceController(vehicleServiceRepository);
    }

    @Bean
    public VehicleRepository vehicleDataSource(VehicleJpaRepository vehicleJpaRepository) {
        return new VehicleRepositoryImpl(vehicleJpaRepository);
    }

    @Bean
    public VehicleController vehicleController(VehicleRepository vehicleRepository) {
        return new VehicleController(vehicleRepository);
    }

    @Bean
    public CustomerRepository customerDataSource(CustomerJpaRepository customerJpaRepository) {
        return new CustomerRepositoryImpl(customerJpaRepository);
    }

    @Bean
    public CustomerController customerController(CustomerRepository customerRepository) {
        return new CustomerController(customerRepository);
    }

    @Bean
    public EmployeeRepository employeeDataSource(EmployeeJpaRepository employeeJpaRepository) {
        return new EmployeeRepositoryImpl(employeeJpaRepository);
    }

    @Bean
    public EmployeeController employeeController(EmployeeRepository employeeRepository, EmployeeFunctionRepository employeeFunctionRepository) {
        return new EmployeeController(employeeRepository, employeeFunctionRepository);
    }

    @Bean
    public EmployeeFunctionRepository employeeFunctionDataSource(EmployeeFunctionJpaRepository employeeFunctionJpaRepository) {
        return new EmployeeFunctionRepositoryImpl(employeeFunctionJpaRepository);
    }

    @Bean
    public EmployeeFunctionController employeeFunctionController(EmployeeFunctionRepository employeeFunctionRepository) {
        return new EmployeeFunctionController(employeeFunctionRepository);
    }

    @Bean
    public ToolCategoryRepository toolCategoryDataSource(ToolCategoryJpaRepository toolCategoryJpaRepository) {
        return new ToolCategoryRepositoryImpl(toolCategoryJpaRepository);
    }

    @Bean
    public ToolCategoryGateway toolCategoryGateway(ToolCategoryRepository toolCategoryRepository) {
        return new ToolCategoryGateway(toolCategoryRepository);
    }

    @Bean
    public ToolCategoryUseCase toolCategoryUseCase(ToolCategoryGateway toolCategoryGateway){
        return new ToolCategoryUseCase(toolCategoryGateway);
    }

    @Bean
    public ToolCategoryPresenter toolCategoryPresenter(){
        return new ToolCategoryPresenter();
    }

    @Bean
    public ToolCategoryController toolCategoryController(ToolCategoryUseCase toolCategoryUseCase, ToolCategoryPresenter toolCategoryPresenter) {
        return new ToolCategoryController(toolCategoryUseCase, toolCategoryPresenter);
    }

    @Bean
    public StockRepository stockDataSource(StockJpaRepository stockJpaRepository) {
        return new StockRepositoryImpl(stockJpaRepository);
    }

    @Bean
    public StockGateway stockGateway(StockRepository stockRepository) {
        return new StockGateway(stockRepository);
    }

    @Bean
    public StockUseCase stockUseCase(StockGateway stockGateway, ToolCategoryGateway toolCategoryGateway) {
        return new StockUseCase(stockGateway, toolCategoryGateway);
    }

    @Bean
    public StockPresenter stockPresenter(ToolCategoryPresenter toolCategoryPresenter) {
        return new StockPresenter(toolCategoryPresenter);
    }

    @Bean
    public StockController stockController(StockUseCase stockUseCase, StockPresenter stockPresenter) {
        return new StockController(stockUseCase, stockPresenter);
    }
}
