package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "service_order_stock")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceOrderStockEntity implements Serializable {

    @EmbeddedId
    private ServiceOrderStockIdEntity id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceOrderId")
    @JoinColumn(name = "service_order_id")
    private ServiceOrderEntity serviceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("stockId")
    @JoinColumn(name = "stock_id")
    private StockEntity stock;

}
