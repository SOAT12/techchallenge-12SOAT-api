package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_order_vehicle_service")
@Getter
@Setter
public class ServiceOrderVehicleService {

    @EmbeddedId
    private ServiceOrderVehicleServiceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceOrderId")
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("vehicleServiceId")
    @JoinColumn(name = "vehicle_service_id")
    private Service service;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_at_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtTime;
}