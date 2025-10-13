package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "service_order_vehicle_service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderVehicleServiceEntity implements Serializable {

    @EmbeddedId
    private ServiceOrderVehicleServiceIdEntity id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceOrderId")
    @JoinColumn(name = "service_order_id")
    private ServiceOrderEntity serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vehicleServiceId")
    @JoinColumn(name = "vehicle_service_id")
    private VehicleServiceJpaEntity vehicleService;

}