package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tool_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCategoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "tool_category_name", nullable = false, unique = true)
    private String toolCategoryName;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
