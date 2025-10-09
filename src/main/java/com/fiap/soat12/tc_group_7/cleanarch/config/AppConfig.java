package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.controller.CustomerController;
import com.fiap.soat12.tc_group_7.cleanarch.controller.VehicleController;
import com.fiap.soat12.tc_group_7.cleanarch.controller.VehicleServiceController;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.CustomerGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleGateway;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.VehicleServiceGateway;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.customer.CustomerRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle.VehicleRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicleservice.VehicleServiceRepositoryImpl;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.CustomerRepository;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleRepository;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.VehicleServiceRepository;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.CustomerPresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehiclePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.presenter.VehicleServicePresenter;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.CustomerUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleServiceUseCase;
import com.fiap.soat12.tc_group_7.cleanarch.usecase.VehicleUseCase;
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

}
