package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle;
import com.fiap.soat12.tc_group_7.util.Status;

import java.util.List;
import java.util.Optional;

public interface ServiceOrderRepository {

    List<ServiceOrder> findAll();

    Optional<ServiceOrder> findById(Long id);

    Long countByEmployeeAndStatusIn(Employee employee, List<Status> statusList);

    List<ServiceOrder> findByEmployeeAndStatusIn(Employee employee, List<Status> statusList);

    List<ServiceOrder> findByCustomerAndFinishedAtIsNull(Customer customer);

    ServiceOrder findByVehicleAndFinishedAtIsNull(Vehicle vehicle);

    ServiceOrder save(ServiceOrder serviceOrder);

}
