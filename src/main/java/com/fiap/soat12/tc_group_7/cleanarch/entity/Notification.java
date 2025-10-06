package com.fiap.soat12.tc_group_7.cleanarch.entity;

import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    private Long id;

    private String message;

    private boolean isRead;

    private ServiceOrder serviceOrder;

    private Set<Employee> employees;

    private Date createdAt;

    private Date updatedAt;
    
}
