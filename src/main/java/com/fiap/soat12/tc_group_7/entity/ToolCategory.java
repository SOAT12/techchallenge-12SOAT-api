package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tool_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tool_category_name", nullable = false, unique = true)
    private String toolCategoryName;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
