package com.fiap.soat12.tc_group_7.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrderRequestDTO {

    @NotNull
    private Long customerId;

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long employeeId;

//        private Long mechanicId;

//        @NotNull
//        private BigDecimal totalValue;

    private String notes;

    private List<VehicleServiceItemDTO> services;

    private List<StockItemDTO> stockItems;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VehicleServiceItemDTO {
        @NotNull
        private Long serviceId;

        @NotNull
        private Integer quantity;

        @NotNull
        private BigDecimal priceAtTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockItemDTO {
        @NotNull
        private Long stockId;

        @NotNull
        private Integer requiredQuantity;
    }
}
