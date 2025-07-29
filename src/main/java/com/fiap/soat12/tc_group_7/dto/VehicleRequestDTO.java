package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDTO {

    @NotBlank(message = "A placa do veículo não pode estar em branco.")
    @Size(max = 20, message = "A placa do veículo não pode exceder 20 caracteres.")
    private String licensePlate;

    @NotBlank(message = "O nome da marca do veículo não pode estar em branco.")
    @Size(max = 50, message = "O nome da marca do veículo não pode exceder 50 caracteres.")
    private String brand;

    @NotBlank(message = "O nome do modelo do veículo não pode estar em branco.")
    @Size(max = 255, message = "O nome do modelo do veículo não pode exceder 50 caracteres.")
    private String model;

    @NotNull(message = "O ano do veículo não pode ser nulo.")
    @Min(value = 1900, message = "O ano do veículo não pode ser anterior a 1900.")
    private Integer year;

    @NotBlank(message = "O nome da cor do veículo não pode estar em branco.")
    @Size(max = 30, message = "O nome da cor do veículo não pode exceder 30 caracteres.")
    private String color;

    private Boolean active = true;
}
