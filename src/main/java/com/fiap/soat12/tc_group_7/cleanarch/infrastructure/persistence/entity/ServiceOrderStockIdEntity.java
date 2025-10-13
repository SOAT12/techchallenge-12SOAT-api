package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceOrderStockIdEntity implements Serializable {

    @Column(name = "service_order_id")
    private Long serviceOrderId;

    @Column(name = "stock_id")
    private UUID stockId;

}
