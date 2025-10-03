package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;


import com.fiap.soat12.tc_group_7.entity.Audit;
import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

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

}

