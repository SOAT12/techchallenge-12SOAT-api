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
            "WHERE s.status IN :statuses " +
            "ORDER BY CASE s.status " +
            "WHEN IN_EXECUTION THEN 1 " +
            "WHEN WAITING_FOR_APPROVAL THEN 2 " +
            "WHEN WAITING_ON_STOCK THEN 3 " +
            "WHEN IN_DIAGNOSIS THEN 4 " +
            "WHEN APPROVED THEN 5 " +
            "WHEN OPENED THEN 6 " +
            "ELSE 7 END, s.createdAt ASC")
    List<ServiceOrderEntity> findAllFilteredAndSorted(List<Status> statuses);

    Long countByEmployeeAndStatusIn(EmployeeJpaEntity employee, List<Status> statusList);

    List<ServiceOrderEntity> findByEmployeeAndStatusIn(EmployeeJpaEntity employee, List<Status> statusList);

    List<ServiceOrderEntity> findByCustomerAndFinishedAtIsNull(CustomerJpaEntity customer);

    List<ServiceOrderEntity> findByVehicleAndFinishedAtIsNull(VehicleJpaEntity vehicle);
}
