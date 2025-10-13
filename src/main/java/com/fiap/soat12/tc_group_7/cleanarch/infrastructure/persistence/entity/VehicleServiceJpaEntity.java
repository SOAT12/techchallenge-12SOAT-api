package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import com.fiap.soat12.tc_group_7.entity.Audit;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vehicle_service")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleServiceJpaEntity extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(nullable = false)
    private Boolean active;

}
