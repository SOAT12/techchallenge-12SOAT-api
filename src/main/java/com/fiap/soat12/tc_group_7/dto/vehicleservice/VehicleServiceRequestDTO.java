package com.fiap.soat12.tc_group_7.dto.vehicleservice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleServiceRequestDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    @NotNull(message = "O valor não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal value;
}
