package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.*;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.ServiceOrderMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ServiceOrderJpaRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.specification.ServiceOrderSpecification;
import com.fiap.soat12.tc_group_7.util.Status;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ServiceOrderRepositoryImpl implements ServiceOrderRepository {

    private final ServiceOrderJpaRepository serviceOrderJpaRepository;
    private final EntityManager entityManager;
    private final ServiceOrderMapper serviceOrderMapper;
    private final EmployeeMapper employeeMapper;
    private final CustomerMapper customerMapper;
    private final VehicleMapper vehicleMapper;

    @Override
    public List<ServiceOrder> findAll() {
        return serviceOrderJpaRepository.findAll().stream()
                .map(serviceOrderMapper::toServiceOrder)
                .toList();
    }

    @Override
    public List<ServiceOrder> findAllWithFilters(Date startDate, Date endDate, List<Long> serviceIds) {
        return serviceOrderJpaRepository.findAll(
                        ServiceOrderSpecification.withFilters(startDate, endDate, serviceIds)
                ).stream()
                .map(serviceOrderMapper::toServiceOrder)
                .toList();
    }

    @Override
    public Optional<ServiceOrder> findById(Long id) {
        return serviceOrderJpaRepository.findById(id)
                .map(serviceOrderMapper::toServiceOrder);
    }

    @Override
    public Long countByEmployeeAndStatusIn(Employee employee, List<Status> statusList) {
        return serviceOrderJpaRepository.countByEmployeeAndStatusIn(employeeMapper.toEmployeeJpaEntity(employee), statusList);
    }

    @Override
    public List<ServiceOrder> findByEmployeeAndStatusIn(Employee employee, List<Status> statusList) {
        var serviceOrders = serviceOrderJpaRepository.findByEmployeeAndStatusIn(employeeMapper.toEmployeeJpaEntity(employee), statusList);
        return serviceOrders.stream()
                .map(serviceOrderMapper::toServiceOrder)
                .toList();
    }

    @Override
    public List<ServiceOrder> findByCustomerAndFinishedAtIsNull(Customer customer) {
        var serviceOrders = serviceOrderJpaRepository.findByCustomerAndFinishedAtIsNull(customerMapper.toCustomerJpaEntity(customer));
        return serviceOrders.stream()
                .map(serviceOrderMapper::toServiceOrder)
                .toList();
    }

    @Override
    public List<ServiceOrder> findByVehicleAndFinishedAtIsNull(Vehicle vehicle) {
        return serviceOrderJpaRepository.findByVehicleAndFinishedAtIsNull(vehicleMapper.toVehicleJpaEntity(vehicle)).stream()
                .map(serviceOrderMapper::toServiceOrder)
                .toList();
    }

    @Override
    public ServiceOrder save(ServiceOrder serviceOrder) {
        var customer = entityManager.getReference(CustomerJpaEntity.class, serviceOrder.getCustomer().getId());
        var vehicle = entityManager.getReference(VehicleJpaEntity.class, serviceOrder.getVehicle().getId());
        var employee = entityManager.getReference(EmployeeJpaEntity.class, serviceOrder.getEmployee().getId());
        Set<ServiceOrderVehicleServiceEntity> services = serviceOrder.getServices().stream()
                .map(service -> {
                    var vehicleServiceJpa = entityManager.getReference(VehicleServiceJpaEntity.class, service.getId());

                    var id = new ServiceOrderVehicleServiceIdEntity(null, service.getId());
                    return new ServiceOrderVehicleServiceEntity(id, null, vehicleServiceJpa);
                }).collect(Collectors.toSet());

        Set<ServiceOrderStockEntity> stockItems = serviceOrder.getStockItems().stream()
                .map(stock -> {
                    var stockJpa = entityManager.getReference(StockEntity.class, stock.getId());

                    var id = new ServiceOrderStockIdEntity(null, stock.getId());
                    return new ServiceOrderStockEntity(id, null, stockJpa);
                }).collect(Collectors.toSet());

        var entity = serviceOrderMapper.toServiceOrderEntity(serviceOrder, customer, vehicle, employee, services, stockItems);
        var savedServiceOrder = serviceOrderJpaRepository.save(entity);
        return serviceOrderMapper.toServiceOrder(savedServiceOrder);
    }

}
