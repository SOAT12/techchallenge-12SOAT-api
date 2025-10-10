package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.vehicle;

import com.fiap.soat12.tc_group_7.entity.Audit;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Table(name = "vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleJpaEntity extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "color", nullable = false)
    private String color;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
