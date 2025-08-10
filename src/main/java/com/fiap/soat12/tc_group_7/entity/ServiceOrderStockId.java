package com.fiap.soat12.tc_group_7.entity;

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
public class ServiceOrderStockId implements Serializable {

    @Column(name = "service_order_id")
    private Long serviceOrderId;

    @Column(name = "stock_id")
    private Long stockId;
}
