package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
