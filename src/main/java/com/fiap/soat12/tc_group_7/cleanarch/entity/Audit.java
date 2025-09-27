package com.fiap.soat12.tc_group_7.cleanarch.entity;

import lombok.Data;

import java.util.Date;

@Data
public abstract class Audit {

    private Date createdAt;

    private Date updatedAt;

    public Audit() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

}
