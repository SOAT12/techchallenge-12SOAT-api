package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.notification.NotificationJpaEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    List<NotificationJpaEntity> findAll();

    Optional<NotificationJpaEntity> findById(Long id);

    List<NotificationJpaEntity> findByEmployees_Id(Long employeeId);

    NotificationJpaEntity save(NotificationJpaEntity notification);

    void delete(NotificationJpaEntity notificationJpaEntity);

}
