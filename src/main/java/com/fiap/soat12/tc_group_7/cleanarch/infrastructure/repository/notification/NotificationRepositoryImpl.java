package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.notification;

import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public List<NotificationJpaEntity> findAll() {
        return notificationJpaRepository.findAll();
    }

    @Override
    public Optional<NotificationJpaEntity> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public List<NotificationJpaEntity> findByEmployees_Id(Long employeeId) {
        return notificationJpaRepository.findByEmployees_Id(employeeId);
    }

    @Override
    public NotificationJpaEntity save(NotificationJpaEntity notification) {
        return notificationJpaRepository.save(notification);
    }

    @Override
    public void delete(NotificationJpaEntity notificationJpaEntity) {
        notificationJpaRepository.delete(notificationJpaEntity);
    }

}
