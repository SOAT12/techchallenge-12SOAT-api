package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleJpaRepository extends JpaRepository<VehicleJpaEntity, Long> {

    Optional<VehicleJpaEntity> findByLicensePlate(String licensePlate);

}
