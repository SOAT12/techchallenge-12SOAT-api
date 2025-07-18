package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tool_name", nullable = false)
    private String toolName;

    @Column(name = "value", nullable = false, precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "tool_category_id", nullable = false)
    private ToolCategory toolCategory;

    public Stock(String toolName, BigDecimal value, Boolean active, Integer quantity, ToolCategory toolCategory) {
        this.toolName = toolName;
        this.value = value;
        this.active = active;
        this.quantity = quantity;
        this.toolCategory = toolCategory;
    }
}
