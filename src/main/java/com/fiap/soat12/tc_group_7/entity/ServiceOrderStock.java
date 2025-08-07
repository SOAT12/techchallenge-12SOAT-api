package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "service_order_stock")
@Getter
@Setter
public class ServiceOrderStock {

    @EmbeddedId
    private ServiceOrderStockId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceOrderId")
    @JoinColumn(name = "service_order_id")
    private ServiceOrder serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stockId")
    @JoinColumn(name = "stock_id")
    private Stock stock;

//    @Column(name = "required_quantity", nullable = false)
//    private Integer requiredQuantity;
}
