package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import com.fiap.soat12.tc_group_7.entity.Audit;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "notification")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationJpaEntity extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_order_id", nullable = false)
    private ServiceOrder serviceOrder;

    @ManyToMany
    @JoinTable(
            name = "notification_employee",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private Set<Employee> employees = new HashSet<>();

}
