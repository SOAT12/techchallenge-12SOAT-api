package com.fiap.soat12.tc_group_7.cleanarch.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Customer {

    private Long id;

    private String cpf;

    private String name;

    private String phone;

    private String email;

    private String city;

    private String state;

    private String district;

    private String street;

    private String number;

    private Boolean deleted;

    private Date createdAt;

    private Date updatedAt;

}
