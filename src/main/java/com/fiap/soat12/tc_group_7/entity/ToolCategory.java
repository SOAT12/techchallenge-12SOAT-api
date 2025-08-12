package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "tool_category")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tool_category_name", nullable = false, unique = true)
    private String toolCategoryName;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
