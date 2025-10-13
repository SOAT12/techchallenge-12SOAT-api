package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, Long>, JpaSpecificationExecutor<ServiceOrderEntity> {

    Long countByEmployeeAndStatusIn(EmployeeJpaEntity employee, List<Status> statusList);

    List<ServiceOrderEntity> findByEmployeeAndStatusIn(EmployeeJpaEntity employee, List<Status> statusList);

    List<ServiceOrderEntity> findByCustomerAndFinishedAtIsNull(CustomerJpaEntity customer);

    List<ServiceOrderEntity> findByVehicleAndFinishedAtIsNull(VehicleJpaEntity vehicle);
}
