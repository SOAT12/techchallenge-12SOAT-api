package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.ServiceOrderRepository;
import com.fiap.soat12.tc_group_7.util.Status;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ServiceOrderGateway {

    private final ServiceOrderRepository serviceOrderRepository;

    public List<ServiceOrder> findAll() {
        return serviceOrderRepository.findAll();
    }

    public List<ServiceOrder> findAllFilteredAndSorted(List<Status> statuses) {
        return serviceOrderRepository.findAllFilteredAndSorted(statuses);
    }

    public List<ServiceOrder> findAllWithFilters(Date startDate, Date endDate, List<Long> serviceIds) {
        return serviceOrderRepository.findAllWithFilters(startDate, endDate, serviceIds);
    }

    public Optional<ServiceOrder> findById(Long id) {
        return serviceOrderRepository.findById(id);
    }

    public Long countByEmployeeAndStatusIn(Employee employee, List<Status> statusList) {
        return serviceOrderRepository.countByEmployeeAndStatusIn(employee, statusList);
    }

    public List<ServiceOrder> findByEmployeeAndStatusIn(Employee employee, List<Status> statusList) {
        return serviceOrderRepository.findByEmployeeAndStatusIn(employee, statusList);
    }

    public List<ServiceOrder> findByCustomerAndFinishedAtIsNull(Customer customer) {
        return serviceOrderRepository.findByCustomerAndFinishedAtIsNull(customer);
    }

    public List<ServiceOrder> findByVehicleAndFinishedAtIsNull(Vehicle vehicle) {
        return serviceOrderRepository.findByVehicleAndFinishedAtIsNull(vehicle);
    }

    public ServiceOrder save(ServiceOrder serviceOrder) {
        return serviceOrderRepository.save(serviceOrder);
    }

}
