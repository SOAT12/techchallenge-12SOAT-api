package com.fiap.soat12.tc_group_7.cleanarch.domain.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository {

    List<Notification> findAll();

    Optional<Notification> findById(Long id);

    List<Notification> findByEmployees_Id(Long employeeId);

    Notification save(Notification notification);

    void delete(Notification notification);

}
