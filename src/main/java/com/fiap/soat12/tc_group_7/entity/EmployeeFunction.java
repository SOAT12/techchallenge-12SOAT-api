package com.fiap.soat12.tc_group_7.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_function")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFunction extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String description;

    @Column(nullable = false)
    private Boolean active;
}
