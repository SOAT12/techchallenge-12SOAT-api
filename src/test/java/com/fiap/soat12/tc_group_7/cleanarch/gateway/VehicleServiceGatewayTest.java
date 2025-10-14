package com.fiap.soat12.tc_group_7.cleanarch.gateway;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.VehicleService;
import com.fiap.soat12.tc_group_7.cleanarch.domain.repository.VehicleServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        List<VehicleService> expected = List.of(
                VehicleService.builder()
                        .id(1L)
                        .name("Troca de óleo")
                        .value(new BigDecimal("150.00"))
                        .active(true)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build(),
                VehicleService.builder()
                        .id(2L)
                        .name("Alinhamento")
                        .value(new BigDecimal("80.00"))
                        .active(true)
                        .createdAt(new Date())
                        .updatedAt(new Date())
                        .build()
        );
        when(repository.findAll()).thenReturn(expected);

        List<VehicleService> result = gateway.findAll();

        assertEquals(expected, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnVehicleServiceIfExists() {
        VehicleService expected = VehicleService.builder()
                .id(1L)
                .name("Balanceamento")
                .value(new BigDecimal("60.00"))
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(expected));

        Optional<VehicleService> result = gateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(repository).findById(1L);
    }

    @Test
    void findById_shouldReturnEmptyIfNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Optional<VehicleService> result = gateway.findById(999L);

        assertTrue(result.isEmpty());
        verify(repository).findById(999L);
    }

    @Test
    void findByName_shouldReturnVehicleServiceIfExists() {
        VehicleService expected = VehicleService.builder()
                .id(1L)
                .name("Revisão")
                .value(new BigDecimal("60.00"))
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(repository.findByName("Revisão")).thenReturn(Optional.of(expected));

        Optional<VehicleService> result = gateway.findByName("Revisão");

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
        verify(repository).findByName("Revisão");
    }

    @Test
    void save_shouldReturnId() {
        VehicleService service = VehicleService.builder()
                .name("Troca de filtro")
                .value(new BigDecimal("90.00"))
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        when(repository.save(service)).thenReturn(10L);

        Long id = gateway.save(service);

        assertEquals(10L, id);
        verify(repository).save(service);
    }

    @Test
    void update_shouldDelegateToRepository() {
        VehicleService updated = VehicleService.builder()
                .id(5L)
                .name("Lavagem completa")
                .value(new BigDecimal("50.00"))
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        gateway.update(updated);

        verify(repository).update(updated);
    }

}
