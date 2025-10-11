package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public abstract class Audit implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The timestamp when the entity was first created.
     * This field is not updatable after its initial insertion.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    /**
     * The timestamp when the entity was last updated.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    private Date updatedAt;

    /**
     * This method is automatically called by the persistence provider (like Hibernate)
     * right before the entity is first saved (persisted) to the database.
     * It sets both the creation and update timestamps to the current time.
     */
    @PrePersist
    protected void onCreate() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * This method is automatically called by the persistence provider
     * before an existing entity is updated in the database.
     * It updates the 'updatedAt' timestamp to the current time.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = new Date();
    }

    @Override
    public String toString() {
        return "Audit{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
