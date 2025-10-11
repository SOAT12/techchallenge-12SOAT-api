package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, Long> {

    List<NotificationJpaEntity> findByEmployees_Id(Long employeeId);

}
