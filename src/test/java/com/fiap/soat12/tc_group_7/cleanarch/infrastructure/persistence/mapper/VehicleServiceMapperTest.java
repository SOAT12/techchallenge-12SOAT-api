package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleServiceMapperTest {

    private final VehicleServiceMapper mapper = new VehicleServiceMapper();

    @Test
    void toVehicleServiceJpaEntity_shouldMapCorrectly() {
        // Arrange
        VehicleService service = VehicleService.builder()
                .id(1L)
                .name("Troca de óleo")
                .value(new BigDecimal("199.90"))
                .active(true)
                .build();

        // Act
        VehicleServiceJpaEntity entity = mapper.toVehicleServiceJpaEntity(service);

        // Assert
        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals("Troca de óleo", entity.getName());
        assertEquals(new BigDecimal("199.90"), entity.getValue());
        assertTrue(entity.getActive());
    }

    @Test
    void toVehicleService_shouldMapCorrectly() {
        // Arrange
        VehicleServiceJpaEntity entity = VehicleServiceJpaEntity.builder()
                .id(2L)
                .name("Alinhamento")
                .value(new BigDecimal("120.00"))
                .active(false)
                .build();

        // Act
        VehicleService service = mapper.toVehicleService(entity);

        // Assert
        assertNotNull(service);
        assertEquals(2L, service.getId());
        assertEquals("Alinhamento", service.getName());
        assertEquals(new BigDecimal("120.00"), service.getValue());
        assertFalse(service.getActive());
    }

}
