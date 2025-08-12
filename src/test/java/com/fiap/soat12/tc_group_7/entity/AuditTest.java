package com.fiap.soat12.tc_group_7.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class AuditTest {

    private static class ConcreteAudit extends Audit {
        // Classe concreta para testar a classe abstrata Audit
    }

    @Test
    @DisplayName("Deve preencher createdAt e updatedAt com a data atual no onCreate")
    void onCreate_shouldSetTimestamps() {
        // Arrange
        ConcreteAudit audit = new ConcreteAudit();

        // Act
        audit.onCreate();

        // Assert
        assertNotNull(audit.getCreatedAt());
        assertNotNull(audit.getUpdatedAt());
        assertEquals(audit.getCreatedAt(), audit.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve atualizar apenas updatedAt e não createdAt no onUpdate")
    void onUpdate_shouldUpdateOnlyUpdatedAt() {
        Date initialDate = new Date(1672531200000L);

        try (MockedStatic<Date> mockedDate = mockStatic(Date.class)) {

            ConcreteAudit audit = new ConcreteAudit();
            audit.setCreatedAt(initialDate);

            audit.onUpdate();

            assertNotNull(audit.getUpdatedAt());
            assertEquals(initialDate, audit.getCreatedAt());
        }
    }

    @Test
    @DisplayName("Deve gerar a representação de String correta")
    void toString_shouldReturnCorrectFormat() {
        // Arrange
        ConcreteAudit audit = new ConcreteAudit();
        Date testDate = new Date(123456789L);
        audit.setCreatedAt(testDate);
        audit.setUpdatedAt(testDate);

        // Act
        String expected = "Audit{createdAt=" + testDate + ", updatedAt=" + testDate + '}';

        // Assert
        assertEquals(expected, audit.toString());
    }
}