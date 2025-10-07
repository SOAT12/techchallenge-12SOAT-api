package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.controller.*;
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


}
