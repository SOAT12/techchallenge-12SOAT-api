package com.fiap.soat12.tc_group_7.dto.vehicleservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleServiceResponseDTO {

    private Long id;

    private String name;

    private BigDecimal value;

}
