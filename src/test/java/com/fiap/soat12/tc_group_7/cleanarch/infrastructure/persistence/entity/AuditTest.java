package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditTest {

    private static class ConcreteAudit extends Audit {}

    private ConcreteAudit audit;

    @BeforeEach
    void setUp() {
        audit = new ConcreteAudit();
    }

    @Nested
    class OnCreate {
        @Test
        void shouldSetCreationAndUpdateDates() {
            // Act
            audit.onCreate();

            // Assert
            assertNotNull(audit.getCreatedAt());
            assertNotNull(audit.getUpdatedAt());
            assertEquals(audit.getCreatedAt(), audit.getUpdatedAt());
        }
    }

    @Nested
    class OnUpdate {
        @Test
        void shouldSetUpdateDate() throws InterruptedException {
            // Arrange
            audit.onCreate();
            Thread.sleep(10);

            // Act
            audit.onUpdate();

            // Assert
            assertNotNull(audit.getUpdatedAt());
            assertTrue(audit.getUpdatedAt().after(audit.getCreatedAt()));
        }
    }
}
