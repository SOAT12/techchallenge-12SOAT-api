package com.fiap.soat12.tc_group_7.cleanarch.domain.model;

import com.fiap.soat12.tc_group_7.cleanarch.util.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrder {

    private Long id;

    private Date finishedAt;

    private Status status;

    private BigDecimal totalValue;

    private String notes;

    private Customer customer;

    private Vehicle vehicle;

    private Employee employee;

    private Set<VehicleService> services = new HashSet<>();
    ;

    private Set<Stock> stockItems = new HashSet<>();

    private Date createdAt;

    private Date updatedAt;

    public BigDecimal calculateTotalValue(Set<VehicleService> services, Set<Stock> stockItems) {
        BigDecimal totalValue = BigDecimal.ZERO;
        if (services == null && stockItems == null) {
            return totalValue;
        }
        if (services != null) {
            totalValue.add(services.stream()
                    .map(VehicleService::getValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        if (stockItems != null) {
            // Calculate total from stock items (value * quantity)
            totalValue.add(stockItems.stream()
                    .map(item -> item.getValue().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        return totalValue;
    }

}
