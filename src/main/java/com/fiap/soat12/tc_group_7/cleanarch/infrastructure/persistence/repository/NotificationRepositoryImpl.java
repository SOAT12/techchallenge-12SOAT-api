package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Notification;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.NotificationRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.NotificationMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.NotificationJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final EntityManager entityManager;
    private final NotificationMapper notificationMapper;
    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public List<Notification> findAll() {
        return notificationJpaRepository.findAll().stream()
                .map(notificationMapper::toNotification)
                .toList();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id)
                .map(notificationMapper::toNotification);
    }

    @Override
    public List<Notification> findByEmployees_Id(Long employeeId) {
        return notificationJpaRepository.findByEmployees_Id(employeeId).stream()
                .map(notificationMapper::toNotification)
                .toList();
    }

    @Override
    public Notification save(Notification notification) {
        var serviceOrderEntity = entityManager.getReference(ServiceOrderEntity.class, notification.getServiceOrder().getId());
        var employeesEntities = notification.getEmployees().stream()
                .map(employee -> entityManager.getReference(EmployeeJpaEntity.class, employee.getId()))
                .collect(Collectors.toSet());
        var savedNotification = notificationJpaRepository.save(notificationMapper.toNotificationJpaEntity(notification, serviceOrderEntity, employeesEntities));
        return notificationMapper.toNotification(savedNotification);
    }

    @Override
    public void delete(Notification notification) {
        var serviceOrderEntity = entityManager.getReference(ServiceOrderEntity.class, notification.getServiceOrder().getId());
        var employeesEntities = notification.getEmployees().stream()
                .map(employee -> entityManager.getReference(EmployeeJpaEntity.class, employee.getId()))
                .collect(Collectors.toSet());
        notificationJpaRepository.delete(notificationMapper.toNotificationJpaEntity(notification, serviceOrderEntity, employeesEntities));
    }

}
