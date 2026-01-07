package com.fiap.soat12.tc_group_7.cleanarch.config;

import com.fiap.soat12.tc_group_7.cleanarch.domain.port.CodeGeneratorPort;
import com.fiap.soat12.tc_group_7.cleanarch.domain.port.EncryptionPort;
import com.fiap.soat12.tc_group_7.cleanarch.domain.port.NotificationPort;
import com.fiap.soat12.tc_group_7.cleanarch.domain.port.TokenServicePort;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.*;
import com.fiap.soat12.tc_group_7.cleanarch.domain.useCases.*;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.*;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter.BCryptAdapter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter.CodeGeneratorAdapter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter.JwtAdapter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter.MailClientAdapter;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.*;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.*;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.*;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.controller.*;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.web.presenter.*;
import com.fiap.soat12.tc_group_7.cleanarch.util.JwtTokenUtil;
import com.fiap.soat12.tc_group_7.config.SessionToken;
import com.fiap.soat12.tc_group_7.service.MailClient;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public VehicleServiceRepository vehicleServiceDataSource(VehicleServiceMapper vehicleServiceMapper, VehicleServiceJpaRepository vehicleServiceJpaRepository) {
        return new VehicleServiceRepositoryImpl(vehicleServiceMapper, vehicleServiceJpaRepository);
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
    public VehicleRepository vehicleDataSource(VehicleMapper vehicleMapper, VehicleJpaRepository vehicleJpaRepository) {
        return new VehicleRepositoryImpl(vehicleMapper, vehicleJpaRepository);
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
    public CustomerRepository customerDataSource(CustomerMapper customerMapper, CustomerJpaRepository customerJpaRepository) {
        return new CustomerRepositoryImpl(customerMapper, customerJpaRepository);
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
    public EmployeeGateway employeeGateway(EmployeeRepository employeeRepository) {
        return new EmployeeGateway(employeeRepository);
    }

    @Bean
    public EmployeePresenter employeePresenter(EmployeeFunctionPresenter employeeFunctionPresenter) {
        return new EmployeePresenter(employeeFunctionPresenter);
    }

    @Bean
    public EmployeeUseCase employeeUseCase(EmployeeGateway employeeGateway, EmployeeFunctionGateway employeeFunctionGateway, EmployeePresenter employeePresenter) {
        return new EmployeeUseCase(employeeGateway, employeeFunctionGateway, employeePresenter);
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
    public EmployeeFunctionGateway employeeFunctionGateway(EmployeeFunctionRepository employeeFunctionRepository) {
        return new EmployeeFunctionGateway(employeeFunctionRepository);
    }

    @Bean
    public EmployeeFunctionPresenter employeeFunctionPresenter() {
        return new EmployeeFunctionPresenter();
    }

    @Bean
    public EmployeeFunctionController employeeFunctionController(EmployeeFunctionRepository employeeFunctionRepository) {
        return new EmployeeFunctionController(employeeFunctionRepository);
    }

    @Bean
    public NotificationRepository notificationDataSource(NotificationJpaRepository notificationJpaRepository, NotificationMapper notificationMapper, EntityManager entityManager) {
        return new NotificationRepositoryImpl(entityManager, notificationMapper, notificationJpaRepository);
    }

    @Bean
    public NotificationMapper notificationMapperBean(
            ServiceOrderMapper serviceOrderMapper,
            EmployeeMapper employeeMapper
    ) {
        return new NotificationMapper(serviceOrderMapper, employeeMapper);
    }

    @Bean
    public NotificationGateway notificationGateway(NotificationRepository notificationRepository) {
        return new NotificationGateway(notificationRepository);
    }

    @Bean
    public NotificationUseCase notificationUseCase(NotificationGateway notificationGateway, EmployeeUseCase employeeUseCase) {
        return new NotificationUseCase(notificationGateway, employeeUseCase);
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
    public ToolCategoryUseCase toolCategoryUseCase(ToolCategoryGateway toolCategoryGateway) {
        return new ToolCategoryUseCase(toolCategoryGateway);
    }

    @Bean
    public ToolCategoryPresenter toolCategoryPresenter() {
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

    @Bean
    public CustomerMapper customerMapperBean() {
        return new CustomerMapper();
    }

    @Bean
    public VehicleMapper vehicleMapper() {
        return new VehicleMapper();
    }

    @Bean
    public EmployeeMapper employeeMapperBean() {
        return new EmployeeMapper();
    }

    @Bean
    public VehicleServiceMapper vehicleServiceMapperBean() {
        return new VehicleServiceMapper();
    }

    @Bean
    public StockMapper stockMapper() {
        return new StockMapper();
    }

    @Bean
    public ServiceOrderMapper serviceOrderMapper(
            CustomerMapper customerMapper,
            VehicleMapper vehicleMapper,
            EmployeeMapper employeeMapper,
            VehicleServiceMapper vehicleServiceMapper,
            StockMapper stockMapper
    ) {
        return new ServiceOrderMapper(customerMapper, vehicleMapper, employeeMapper, vehicleServiceMapper, stockMapper);
    }

    @Bean
    public ServiceOrderRepository serviceOrderDataSource(ServiceOrderJpaRepository serviceOrderJpaRepository,
                                                         ServiceOrderMapper serviceOrderMapper,
                                                         EmployeeMapper employeeMapper,
                                                         CustomerMapper customerMapper,
                                                         VehicleMapper vehicleMapper,
                                                         EntityManager entityManager) {
        return new ServiceOrderRepositoryImpl(serviceOrderJpaRepository, entityManager, serviceOrderMapper, employeeMapper, customerMapper, vehicleMapper);
    }

    @Bean
    public ServiceOrderGateway serviceOrderGateway(ServiceOrderRepository serviceOrderRepository) {
        return new ServiceOrderGateway(serviceOrderRepository);
    }

    @Bean
    public ServiceOrderUseCase serviceOrderUseCase(ServiceOrderGateway serviceOrderGateway,
                                                   EmployeeUseCase employeeUseCase,
                                                   CustomerUseCase customerUseCase,
                                                   NotificationUseCase notificationUseCase,
                                                   VehicleUseCase vehicleUseCase,
                                                   VehicleServiceUseCase vehicleServiceUseCase,
                                                   StockUseCase stockUseCase,
                                                   MailClient mailClient,
                                                   MeterRegistry meterRegistry) {
        return new ServiceOrderUseCase(serviceOrderGateway, employeeUseCase, customerUseCase, notificationUseCase, vehicleUseCase, vehicleServiceUseCase, stockUseCase, mailClient, meterRegistry);
    }

    @Bean
    public ServiceOrderPresenter serviceOrderPresenter() {
        return new ServiceOrderPresenter();
    }

    @Bean
    public ServiceOrderController serviceOrderController(ServiceOrderUseCase serviceOrderUseCase, ServiceOrderPresenter serviceOrderPresenter) {
        return new ServiceOrderController(serviceOrderUseCase, serviceOrderPresenter);
    }

    @Bean
    public EncryptionPort encryptionPort() {
        return new BCryptAdapter();
    }

    @Bean
    public TokenServicePort tokenServicePort(JwtTokenUtil jwtTokenUtil, SessionToken sessionToken) {
        return new JwtAdapter(jwtTokenUtil, sessionToken);
    }

    @Bean
    public NotificationPort notificationPort(MailClient mailClient) {
        return new MailClientAdapter(mailClient);
    }

    @Bean
    public CodeGeneratorPort codeGeneratorPort() {
        return new CodeGeneratorAdapter();
    }

    @Bean
    public PasswordManagementUseCase passwordManagementUseCase(
            EmployeeGateway employeeGateway,
            EncryptionPort encryptionPort,
            CodeGeneratorPort codeGeneratorPort,
            NotificationPort notificationPort) {
        return new PasswordManagementUseCase(
                employeeGateway,
                encryptionPort,
                codeGeneratorPort,
                notificationPort);
    }

    @Bean
    public EmployeeAuthUseCase employeeAuthUseCase(
            EmployeeGateway employeeGateway,
            EncryptionPort encryptionPort,
            TokenServicePort tokenServicePort,
            PasswordManagementUseCase passwordManagementUseCase) {
        return new EmployeeAuthUseCase(
                employeeGateway,
                encryptionPort,
                tokenServicePort);
    }
}