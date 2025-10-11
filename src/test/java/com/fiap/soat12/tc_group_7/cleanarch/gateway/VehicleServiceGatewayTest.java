package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleServiceRepository;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleServiceGatewayTest {

    private VehicleServiceRepository repository;
    private VehicleServiceGateway gateway;

    @BeforeEach
    void setUp() {
        repository = mock(VehicleServiceRepository.class);
        gateway = new VehicleServiceGateway(repository);
    }

    @Test
    void findAll_shouldReturnListOfVehicleServices() {
        List<VehicleServiceJpaEntity> entities = List.of(
                createJpaEntity(1L),
                createJpaEntity(2L)
        );

        when(repository.findAll()).thenReturn(entities);

        List<VehicleService> services = gateway.findAll();

        assertEquals(2, services.size());
        verify(repository).findAll();
    }

    @Test
    void findById_shouldReturnVehicleService_whenFound() {
        VehicleServiceJpaEntity entity = createJpaEntity(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<VehicleService> result = gateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(entity.getId(), result.get().getId());
        verify(repository).findById(1L);
    }

    @Test
    void findById_shouldReturnEmpty_whenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<VehicleService> result = gateway.findById(999L);

        assertTrue(result.isEmpty());
    }

    @Test
    void save_shouldReturnGeneratedId() {
        VehicleService service = createDomain(1L);
        when(repository.save(any())).thenReturn(1L);

        Long id = gateway.save(service);

        assertEquals(1L, id);
        verify(repository).save(any());
    }

    @Test
    void update_shouldCallRepositoryUpdate() {
        VehicleService service = createDomain(1L);

        gateway.update(service);

        verify(repository).update(any());
    }

    private VehicleService createDomain(Long id) {
        return VehicleService.builder()
                .id(id)
                .name("Alinhamento")
                .value(BigDecimal.valueOf(99.99))
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    private VehicleServiceJpaEntity createJpaEntity(Long id) {
        return VehicleServiceJpaEntity.builder()
                .id(id)
                .name("Alinhamento")
                .value(BigDecimal.valueOf(99.99))
                .active(true)
                .build();
    }

}
