package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequestDTO {

    @Pattern(regexp = "^([A-Z]{3}-[0-9]{4}|[A-Z]{3}[0-9][A-Z][0-9]{2})$", message = "Formato de placa inserido inválido, favor usar o padrão: 'AAA-9999' ou 'AAA9A99'.")
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
