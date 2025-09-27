package com.fiap.soat12.tc_group_7.cleanarch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VehicleService extends Audit {

    private Long id;

    private String name;

    private BigDecimal value;

    private Boolean active;

}
