package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.controller.*;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.StockRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ToolCategoryRepository;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.StockUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.ToolCategoryUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.*;
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
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.notification.NotificationJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.notification.NotificationRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.StockController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.ToolCategoryController;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.StockPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.ToolCategoryPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.*;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.CustomerPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.NotificationPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehiclePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehicleServicePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.CustomerUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.NotificationUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleServiceUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api.StockApi;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.api.ToolCategoryApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public VehicleServiceRepository vehicleServiceDataSource(VehicleServiceJpaRepository vehicleServiceJpaRepository) {
        return new VehicleServiceRepositoryImpl(vehicleServiceJpaRepository);
    }

    @Bean
    public VehicleServiceGateway vehicleServiceGateway(VehicleServiceRepository vehicleServiceRepository) {
        return new VehicleServiceGateway(vehicleServiceRepository);
    }

    @Bean
    public VehicleServiceUseCase vehicleServiceUseCase(VehicleServiceGateway vehicleServiceGateway) {
        return new VehicleServiceUseCase(vehicleServiceGateway);
    }

    @Bean
    public VehicleServicePresenter vehicleServicePresenter() {
        return new VehicleServicePresenter();
    }

    @Bean
    public VehicleServiceController vehicleServiceController(
            VehicleServiceUseCase vehicleServiceUseCase,
            VehicleServicePresenter vehicleServicePresenter) {
        return new VehicleServiceController(vehicleServiceUseCase, vehicleServicePresenter);
    }

    @Bean
    public VehicleRepository vehicleDataSource(VehicleJpaRepository vehicleJpaRepository) {
        return new VehicleRepositoryImpl(vehicleJpaRepository);
    }

    @Bean
    public VehicleGateway vehicleGateway(VehicleRepository vehicleRepository) {
        return new VehicleGateway(vehicleRepository);
    }

    @Bean
    public VehicleUseCase vehicleUseCase(VehicleGateway vehicleGateway) {
        return new VehicleUseCase(vehicleGateway);
    }

    @Bean
    public VehiclePresenter vehiclePresenter() {
        return new VehiclePresenter();
    }

    @Bean
    public VehicleController vehicleController(VehicleUseCase vehicleUseCase, VehiclePresenter vehiclePresenter) {
        return new VehicleController(vehicleUseCase, vehiclePresenter);
    }

    @Bean
    public CustomerRepository customerDataSource(CustomerJpaRepository customerJpaRepository) {
        return new CustomerRepositoryImpl(customerJpaRepository);
    }

    @Bean
    public CustomerGateway customerGateway(CustomerRepository customerRepository) {
        return new CustomerGateway(customerRepository);
    }

    @Bean
    public CustomerUseCase customerUseCase(CustomerGateway customerGateway) {
        return new CustomerUseCase(customerGateway);
    }

    @Bean
    public CustomerPresenter customerPresenter() {
        return new CustomerPresenter();
    }

    @Bean
    public CustomerController customerController(CustomerUseCase customerUseCase,
                                                 CustomerPresenter customerPresenter) {
        return new CustomerController(customerUseCase, customerPresenter);
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
    public NotificationRepository notificationDataSource(NotificationJpaRepository notificationJpaRepository) {
        return new NotificationRepositoryImpl(notificationJpaRepository);
    }

    @Bean
    public NotificationGateway notificationGateway(NotificationRepository notificationRepository) {
        return new NotificationGateway(notificationRepository);
    }

    @Bean
    public NotificationUseCase notificationUseCase(NotificationGateway notificationGateway) {
        return new NotificationUseCase(notificationGateway);
    }

    @Bean
    public NotificationPresenter notificationPresenter() {
        return new NotificationPresenter();
    }

    @Bean
    public NotificationController notificationController(NotificationUseCase notificationUseCase, NotificationPresenter notificationPresenter) {
        return new NotificationController(notificationUseCase, notificationPresenter);
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
    public ToolCategoryApi toolCategoryApi(ToolCategoryController toolCategoryController) {
        return new ToolCategoryApi(toolCategoryController);
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

    @Bean
    public StockApi stockApi(StockController stockController) {
        return new StockApi(stockController);
    }
}
