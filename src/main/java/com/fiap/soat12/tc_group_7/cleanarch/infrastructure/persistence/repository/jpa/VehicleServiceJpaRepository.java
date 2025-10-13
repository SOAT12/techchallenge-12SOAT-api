package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleServiceJpaRepository extends JpaRepository<VehicleServiceJpaEntity, Long> {

    Optional<VehicleServiceJpaEntity> findByName(String name);

}
