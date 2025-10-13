package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceOrderVehicleServiceIdEntity implements Serializable {

    @Column(name = "service_order_id")
    private Long serviceOrderId;

    @Column(name = "vehicle_service_id")
    private Long vehicleServiceId;
}