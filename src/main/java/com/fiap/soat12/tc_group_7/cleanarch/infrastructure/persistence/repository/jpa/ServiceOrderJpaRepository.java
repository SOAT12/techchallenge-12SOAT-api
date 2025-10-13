package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.soat12.tc_group_7.util.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderEntity, Long>, JpaSpecificationExecutor<ServiceOrderEntity> {

    @Query("SELECT s FROM ServiceOrderEntity s " +
            "WHERE s.status IN (com.fiap.soat12.tc_group_7.util.Status.OPENED, com.fiap.soat12.tc_group_7.util.Status.IN_DIAGNOSIS, com.fiap.soat12.tc_group_7.util.Status.WAITING_FOR_APPROVAL, com.fiap.soat12.tc_group_7.util.Status.APPROVED, com.fiap.soat12.tc_group_7.util.Status.WAITING_ON_STOCK, com.fiap.soat12.tc_group_7.util.Status.IN_EXECUTION) " +
            "ORDER BY CASE s.status " +
            "WHEN com.fiap.soat12.tc_group_7.util.Status.IN_EXECUTION THEN 1 " +
            "WHEN com.fiap.soat12.tc_group_7.util.Status.WAITING_FOR_APPROVAL THEN 2 " +
            "WHEN com.fiap.soat12.tc_group_7.util.Status.WAITING_ON_STOCK THEN 3 " +
            "WHEN com.fiap.soat12.tc_group_7.util.Status.IN_DIAGNOSIS THEN 4 " +
            "WHEN com.fiap.soat12.tc_group_7.util.Status.APPROVED THEN 5 " +
            "WHEN com.fiap.soat12.tc_group_7.util.Status.OPENED THEN 6 " +
            "ELSE 7 END, s.createdAt ASC")
    List<ServiceOrderEntity> findAllFilteredAndSorted();

    Long countByEmployeeAndStatusIn(EmployeeJpaEntity employee, List<Status> statusList);

    List<ServiceOrderEntity> findByEmployeeAndStatusIn(EmployeeJpaEntity employee, List<Status> statusList);

    List<ServiceOrderEntity> findByCustomerAndFinishedAtIsNull(CustomerJpaEntity customer);

    List<ServiceOrderEntity> findByVehicleAndFinishedAtIsNull(VehicleJpaEntity vehicle);
}
