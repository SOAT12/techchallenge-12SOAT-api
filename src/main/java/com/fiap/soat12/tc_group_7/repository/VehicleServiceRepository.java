package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.VehicleService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleServiceRepository extends JpaRepository<VehicleService, Long> {

    List<VehicleService> findAllByActiveTrue();

    Optional<VehicleService> findByIdAndActiveTrue(Long id);

}
