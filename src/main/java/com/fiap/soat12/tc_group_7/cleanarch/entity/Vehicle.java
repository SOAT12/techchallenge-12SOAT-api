package com.fiap.soat12.tc_group_7.cleanarch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Vehicle {

    private Long id;

    private String licensePlate;

    private String brand;

    private String model;

    private Integer year;

    private String color;

    @Builder.Default
    private Boolean active = true;

    private Date createdAt;

    private Date updatedAt;

}
