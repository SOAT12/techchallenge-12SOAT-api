package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import com.fiap.soat12.tc_group_7.entity.Audit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee_function")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFunctionJpaEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String description;

    @Column(nullable = false)
    private Boolean active;
}
