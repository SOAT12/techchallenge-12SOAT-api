package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.employee;

import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.repository.EmployeeFunction.EmployeeFunctionJpaEntity;
import com.fiap.soat12.tc_group_7.entity.Audit;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "employee")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeJpaEntity extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_function_id", nullable = false)
    private EmployeeFunctionJpaEntity employeeFunction;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "temporary_password", nullable = false)
    private String temporaryPassword;

    @Column(name = "password_validity", nullable = false)
    private LocalDateTime passwordValidity;

    @Column(name = "use_temporary_password", nullable = false)
    private Boolean useTemporaryPassword;
}
