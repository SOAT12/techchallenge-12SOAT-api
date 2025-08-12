package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.Customer;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.entity.Vehicle;
import com.fiap.soat12.tc_group_7.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, Long> {

    Long countByEmployeeAndStatusIn(Employee employee, List<Status> statusList);

    List<ServiceOrder> findByEmployeeAndStatusIn(Employee employee, List<Status> statusList);

    List<ServiceOrder> findByCustomerAndFinishedAtIsNull(Customer customer);

    ServiceOrder findByVehicleAndFinishedAtIsNull(Vehicle vehicle);
}
