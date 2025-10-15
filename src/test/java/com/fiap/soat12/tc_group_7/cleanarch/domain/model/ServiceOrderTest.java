package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServiceOrderTest {

    @Nested
    class CalculateTotalValue {

        @Test
        void shouldReturnZeroForNullSets() {
            ServiceOrder serviceOrder = new ServiceOrder();
            BigDecimal result = serviceOrder.calculateTotalValue(null, null);
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        void shouldReturnZeroForEmptySets() {
            ServiceOrder serviceOrder = new ServiceOrder();
            BigDecimal result = serviceOrder.calculateTotalValue(new HashSet<>(), new HashSet<>());
            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        void shouldCalculateValueForServicesOnly() {
            ServiceOrder serviceOrder = new ServiceOrder();
            Set<VehicleService> services = new HashSet<>();
            services.add(VehicleService.builder().value(BigDecimal.TEN).build());
            services.add(VehicleService.builder().value(BigDecimal.ONE).build());

            BigDecimal result = serviceOrder.calculateTotalValue(services, new HashSet<>());

            assertEquals(new BigDecimal("11"), result);
        }

        @Test
        void shouldCalculateValueForStockItemsOnly() {
            ServiceOrder serviceOrder = new ServiceOrder();
            Set<Stock> stockItems = new HashSet<>();
            stockItems.add(Stock.builder().value(BigDecimal.TEN).quantity(2).build()); // 20
            stockItems.add(Stock.builder().value(BigDecimal.ONE).quantity(5).build()); // 5

            BigDecimal result = serviceOrder.calculateTotalValue(new HashSet<>(), stockItems);

            assertEquals(new BigDecimal("20"), result);
        }

        @Test
        void shouldCalculateValueForServicesAndStockItems() {
            ServiceOrder serviceOrder = new ServiceOrder();
            Set<VehicleService> services = new HashSet<>();
            services.add(VehicleService.builder().value(BigDecimal.TEN).build());

            Set<Stock> stockItems = new HashSet<>();
            stockItems.add(Stock.builder().value(BigDecimal.TEN).quantity(2).build());

            BigDecimal result = serviceOrder.calculateTotalValue(services, stockItems);

            assertEquals(new BigDecimal("30"), result);
        }
    }
}
