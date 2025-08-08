package com.fiap.soat12.tc_group_7.entity;

import com.fiap.soat12.tc_group_7.util.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "service_order")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrder extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "finished_at")
    private Date finishedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @Column(name = "total_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalValue;

    @Column(columnDefinition = "TEXT")
    private String notes;

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ServiceOrderVehicleService> services;

    @OneToMany(mappedBy = "serviceOrder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ServiceOrderStock> stockItems;

    public BigDecimal calculateTotalValue(Set<ServiceOrderVehicleService> services, Set<ServiceOrderStock> stockItems) {
        BigDecimal servicesTotal = services.stream()
                .map(service -> service.getVehicleService().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total from stock items (value * quantity)
        BigDecimal stocksTotal = stockItems.stream()
                .map(item -> item.getStock().getValue().multiply(new BigDecimal(item.getStock().getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return servicesTotal.add(stocksTotal);
    }
}
