package com.fiap.soat12.tc_group_7.dto.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {

    private Long id;
    private String licensePlate;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private Boolean active = true;
}
