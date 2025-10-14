package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleServiceJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleServiceMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.VehicleServiceJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class VehicleServiceRepositoryImplTest {

    private VehicleServiceMapper mapper;
    private VehicleServiceJpaRepository jpaRepository;
    private VehicleServiceRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        mapper = mock(VehicleServiceMapper.class);
        jpaRepository = mock(VehicleServiceJpaRepository.class);
        repository = new VehicleServiceRepositoryImpl(mapper, jpaRepository);
    }

    @Test
    void findAll_shouldReturnMappedList() {
        var jpaEntities = List.of(new VehicleServiceJpaEntity(), new VehicleServiceJpaEntity());
        var domainEntities = List.of(
                VehicleService.builder().id(1L).name("Troca de óleo").build(),
                VehicleService.builder().id(2L).name("Alinhamento").build()
        );

        when(jpaRepository.findAll()).thenReturn(jpaEntities);
        when(mapper.toVehicleService(jpaEntities.get(0))).thenReturn(domainEntities.get(0));
        when(mapper.toVehicleService(jpaEntities.get(1))).thenReturn(domainEntities.get(1));

        List<VehicleService> result = repository.findAll();

        assertEquals(2, result.size());
        verify(jpaRepository).findAll();
    }

    @Test
    void findById_shouldReturnMappedEntityIfFound() {
        var jpaEntity = new VehicleServiceJpaEntity();
        var domain = VehicleService.builder().id(10L).name("Revisão").build();

        when(jpaRepository.findById(10L)).thenReturn(Optional.of(jpaEntity));
        when(mapper.toVehicleService(jpaEntity)).thenReturn(domain);

        Optional<VehicleService> result = repository.findById(10L);

        assertTrue(result.isPresent());
        assertEquals("Revisão", result.get().getName());
        verify(jpaRepository).findById(10L);
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<VehicleService> result = repository.findById(99L);

        assertTrue(result.isEmpty());
        verify(jpaRepository).findById(99L);
    }

    @Test
    void findByName_shouldReturnMappedEntityIfFound() {
        var jpaEntity = new VehicleServiceJpaEntity();
        var domain = VehicleService.builder().id(3L).name("Lavagem").build();

        when(jpaRepository.findByName("Lavagem")).thenReturn(Optional.of(jpaEntity));
        when(mapper.toVehicleService(jpaEntity)).thenReturn(domain);

        Optional<VehicleService> result = repository.findByName("Lavagem");

        assertTrue(result.isPresent());
        assertEquals("Lavagem", result.get().getName());
        verify(jpaRepository).findByName("Lavagem");
    }

    @Test
    void save_shouldReturnGeneratedId() {
        var domain = VehicleService.builder()
                .name("Pintura")
                .value(new BigDecimal("300.00"))
                .build();

        var jpaEntity = VehicleServiceJpaEntity.builder().id(42L).build();

        when(mapper.toVehicleServiceJpaEntity(domain)).thenReturn(jpaEntity);
        when(jpaRepository.save(jpaEntity)).thenReturn(jpaEntity);

        Long id = repository.save(domain);

        assertEquals(42L, id);
        verify(jpaRepository).save(jpaEntity);
    }

    @Test
    void update_shouldCallSaveWithMappedEntity() {
        var domain = VehicleService.builder()
                .id(77L)
                .name("Revisão Geral")
                .value(new BigDecimal("400.00"))
                .build();

        var jpaEntity = VehicleServiceJpaEntity.builder().id(77L).build();

        when(mapper.toVehicleServiceJpaEntity(domain)).thenReturn(jpaEntity);

        repository.update(domain);

        verify(jpaRepository).save(jpaEntity);
    }

}
