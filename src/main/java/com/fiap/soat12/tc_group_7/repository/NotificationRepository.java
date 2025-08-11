package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByEmployees_Id(Long employeeId);

}
