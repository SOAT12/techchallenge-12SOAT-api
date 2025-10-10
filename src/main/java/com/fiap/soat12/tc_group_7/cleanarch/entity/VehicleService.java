package com.fiap.soat12.tc_group_7.cleanarch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class VehicleService {

    private Long id;

    private String name;

    private BigDecimal value;

    private Boolean active;

    private Date createdAt;

    private Date updatedAt;

}
