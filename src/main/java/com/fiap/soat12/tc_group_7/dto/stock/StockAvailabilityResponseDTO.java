package com.fiap.soat12.tc_group_7.dto.stock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAvailabilityResponseDTO {

    private boolean isAvailable;
    private List<MissingItemDTO> missingItems;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MissingItemDTO {
        private UUID stockId;
        private String name;
        private int requiredQuantity;
        private int availableQuantity;
    }
}
