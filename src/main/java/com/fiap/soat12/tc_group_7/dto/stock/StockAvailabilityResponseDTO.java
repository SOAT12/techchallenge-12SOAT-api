package com.fiap.soat12.tc_group_7.dto.stock;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockAvailabilityResponseDTO {

    private boolean isAvailable; // A quick flag indicating if all items are in stock
    private List<MissingItemDTO> missingItems;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MissingItemDTO {
        private Long stockId;
        private String name;
        private int requiredQuantity;
        private int availableQuantity;
    }
}
