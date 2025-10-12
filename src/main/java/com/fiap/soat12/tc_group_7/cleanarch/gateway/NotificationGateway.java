package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class NotificationGateway {

    private final NotificationRepository notificationRepository;

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    public List<Notification> findByEmployees_Id(Long employeeId) {
        return notificationRepository.findByEmployees_Id(employeeId);
    }

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    public void delete(Notification notification) {
        notificationRepository.delete(notification);
    }

}
