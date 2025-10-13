package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleServiceJpaRepository extends JpaRepository<VehicleServiceJpaEntity, Long> {
}
