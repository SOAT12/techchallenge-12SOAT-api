package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import com.fiap.soat12.tc_group_7.entity.Audit;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "customer")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerJpaEntity extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String city;

    @Column(length = 2)
    private String state;

    @Column(length = 100)
    private String district;

    @Column(length = 100)
    private String street;

    @Column(length = 20)
    private String number;

    private Boolean deleted;

}
