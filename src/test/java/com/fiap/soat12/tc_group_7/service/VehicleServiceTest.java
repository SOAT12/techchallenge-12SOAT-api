package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleRequestDTO;
import com.fiap.soat12.tc_group_7.dto.vehicle.VehicleResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Vehicle;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    private Vehicle vehicleActive;
    private Vehicle vehicleInactive;
    private VehicleRequestDTO vehicleRequestDTO;

    @BeforeEach
    void setUp(){
        vehicleActive = new Vehicle(99L, "QFV-8487", "Chevrolet", "Onix", 2017, "Black", true);
        vehicleInactive = new Vehicle(100L, "ABC-0001", "Volve", "XC40", 2020, "White", false);
        vehicleRequestDTO = new VehicleRequestDTO("RIO2A18", "Honda", "HRV", 2025, "Gray");
    }

    @Test
    @DisplayName("Save Vehicle - Should save a vehicle with success")
    void saveVehicle_Success() {
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> {
            Vehicle v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        VehicleResponseDTO savedVehicle = vehicleService.createVehicle(vehicleRequestDTO);

        assertNotNull(savedVehicle);
        assertEquals(vehicleRequestDTO.getLicensePlate(), savedVehicle.getLicensePlate());
        assertEquals(vehicleRequestDTO.getBrand(), savedVehicle.getBrand());
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Find All Vehicles - Should return a list of vehicles")
    void findAllVehicles_Success() {
        when(vehicleRepository.findAll()).thenReturn(List.of(vehicleActive, vehicleInactive));

        List<VehicleResponseDTO> vehicles = vehicleService.findAll();

        assertNotNull(vehicles);
        assertEquals(2, vehicles.size());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find All Vehicles - Should return a list of vehicles")
    void findAllVehiclesActive_Success() {
        when(vehicleRepository.findAllByActiveTrue()).thenReturn(List.of(vehicleActive));

        List<VehicleResponseDTO> vehicles = vehicleService.findAllVehiclesActive();

        assertNotNull(vehicles);
        assertEquals(1, vehicles.size());
        verify(vehicleRepository, times(1)).findAllByActiveTrue();
    }

    @Test
    @DisplayName("Find All Vehicles - Should return an empty list when no vehicles exist")
    void findAllVehicles_EmptyList() {
        when(vehicleRepository.findAll()).thenReturn(Collections.emptyList());

        List<VehicleResponseDTO> vehicles = vehicleService.findAll();

        assertNotNull(vehicles);
        assertTrue(vehicles.isEmpty());
        verify(vehicleRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find Vehicle By Id - Should return a vehicle when ID exists")
    void findById_Success() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.of(vehicleActive));

        Optional<VehicleResponseDTO> foundVehicle = vehicleService.findById(99L);

        assertTrue(foundVehicle.isPresent());
        assertNotNull(foundVehicle);
        assertEquals(vehicleActive.getId(), foundVehicle.get().getId());
        verify(vehicleRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Find Vehicle By Id - Should throw exception when ID does not exist")
    void findById_NotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<VehicleResponseDTO> result = vehicleService.findById(999L);

        assertFalse(result.isPresent());

        verify(vehicleRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Find Vehicle By License Plate - Should return a vehicle when license plate exists")
    void findByLicensePlate_Success() {
        when(vehicleRepository.findByLicensePlate("QFV-8487")).thenReturn(Optional.of(vehicleActive));

        Optional<VehicleResponseDTO> foundVehicle = vehicleService.findByLicensePlate("QFV-8487");

        assertNotNull(foundVehicle);
        assertTrue(foundVehicle.isPresent());
        assertEquals("QFV-8487", foundVehicle.get().getLicensePlate());
        verify(vehicleRepository, times(1)).findByLicensePlate("QFV-8487");
    }

    @Test
    @DisplayName("Find Vehicle By License Plate - Should throw exception when license plate does not exist")
    void findByLincensePlate_NotFound() {
        when(vehicleRepository.findByLicensePlate(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.findByLicensePlate("XXX-0000"));
        verify(vehicleRepository, times(1)).findByLicensePlate("XXX-0000");

    }

    @Test
    @DisplayName("Update Vehicle - Should update vehicle with success")
    void updateVehicle_Success() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.of(vehicleActive));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<VehicleResponseDTO> updatedVehicle = vehicleService.updateVehicle(99L, vehicleRequestDTO);

        assertNotNull(updatedVehicle);
        assertTrue(updatedVehicle.isPresent());
        assertEquals(vehicleRequestDTO.getBrand(), updatedVehicle.get().getBrand());
        assertEquals(vehicleRequestDTO.getModel(), updatedVehicle.get().getModel());
        assertEquals(vehicleRequestDTO.getColor(), updatedVehicle.get().getColor());
        verify(vehicleRepository, times(1)).findById(99L);
        verify(vehicleRepository, times(1)).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Update Vehicle - Should throw exception when vehicle to update is not found")
    void updateVehicle_NotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.updateVehicle(1L, vehicleRequestDTO));
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Delete Vehicle - Should logically delete a vehicle with success")
    void logicDeleteVehicle_Success() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.of(vehicleActive));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        vehicleService.logicallyDeleteVehicle(99L);

        assertFalse(vehicleActive.getActive());
        verify(vehicleRepository, times(1)).findById(99L);
        verify(vehicleRepository, times(1)).save(vehicleActive);
    }

    @Test
    @DisplayName("Delete Vehicle - Should throw exception when vehicle to delete is not found")
    void logicallyDeleteVehicle_NotFound() {
        when(vehicleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> vehicleService.logicallyDeleteVehicle(1L));
        verify(vehicleRepository, times(1)).findById(1L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }

    @Test
    @DisplayName("Deve reativar um veículo com sucesso")
    void reactiveVehicle_success() {
        when(vehicleRepository.findById(100L)).thenReturn(Optional.of(vehicleInactive));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicleInactive);

        Optional<VehicleResponseDTO> result = vehicleService.reactivateVehicle(100L);

        assertTrue(result.isPresent());
        assertTrue(vehicleInactive.getActive());

        verify(vehicleRepository, times(1)).findById(100L);
        verify(vehicleRepository, times(1)).save(vehicleInactive);
    }

    @Test
    @DisplayName("Não deve reativar veículo se não for encontrado")
    void reactiveVehicle_NotFound() {
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<VehicleResponseDTO> result = vehicleService.reactivateVehicle(999L);

        assertFalse(result.isPresent());

        verify(vehicleRepository, times(1)).findById(999L);
        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
}
