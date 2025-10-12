package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.ServiceOrderMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ServiceOrderJpaRepository;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ServiceOrderRepositoryImpl implements ServiceOrderRepository {

    private final ServiceOrderJpaRepository serviceOrderJpaRepository;
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
    public ServiceOrder findByVehicleAndFinishedAtIsNull(Vehicle vehicle) {
        var savedServiceOrder = serviceOrderJpaRepository.findByVehicleAndFinishedAtIsNull(vehicleMapper.toVehicleJpaEntity(vehicle));
        return serviceOrderMapper.toServiceOrder(savedServiceOrder);
    }

    @Override
    public ServiceOrder save(ServiceOrder serviceOrder) {
        var savedServiceOrder = serviceOrderJpaRepository.save(serviceOrderMapper.toServiceOrderEntity(serviceOrder));
        return serviceOrderMapper.toServiceOrder(savedServiceOrder);
    }

}
