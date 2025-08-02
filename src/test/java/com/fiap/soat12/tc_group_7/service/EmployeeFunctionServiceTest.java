package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.mapper.EmployeeFunctionMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeFunctionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeFunctionServiceTest {

    @Mock
    private EmployeeFunctionRepository employeeFunctionRepository;
    @Mock
    private EmployeeFunctionMapper employeeFunctionMapper;
    @InjectMocks
    private EmployeeFunctionService employeeFunctionService;

    private EmployeeFunction employeeFunction;
    private EmployeeFunctionRequestDTO requestDTO;
    private EmployeeFunctionResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        employeeFunction = EmployeeFunction.builder()
                .id(1L)
                .description("Gerente")
                .active(true)
                .build();
        requestDTO = EmployeeFunctionRequestDTO.builder()
                .description("Gerente")
                .active(true)
                .build();
        responseDTO = EmployeeFunctionResponseDTO.builder()
                .id(1L)
                .description("Gerente")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Deve criar função de funcionário com sucesso")
    void shouldCreateEmployeeFunctionSuccessfully() {
        when(employeeFunctionMapper.toEmployeeFunction(requestDTO)).thenReturn(employeeFunction);
        when(employeeFunctionRepository.save(employeeFunction)).thenReturn(employeeFunction);
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employeeFunction)).thenReturn(responseDTO);
        EmployeeFunctionResponseDTO result = employeeFunctionService.createEmployeeFunction(requestDTO);
        assertNotNull(result);
        assertEquals("Gerente", result.getDescription());
        assertTrue(employeeFunction.getActive());
    }

    @Test
    @DisplayName("Deve retornar função de funcionário por ID")
    void shouldReturnEmployeeFunctionById() {
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employeeFunction)).thenReturn(responseDTO);
        Optional<EmployeeFunctionResponseDTO> result = employeeFunctionService.getEmployeeFunctionById(1L);
        assertTrue(result.isPresent());
        assertEquals("Gerente", result.get().getDescription());
    }

    @Test
    @DisplayName("Deve listar todas as funções de funcionário ativas")
    void shouldListAllActiveEmployeeFunctions() {
        EmployeeFunction inactiveFunction = EmployeeFunction.builder().id(2L).description("Ex-Gerente").active(false).build();
        when(employeeFunctionRepository.findAll()).thenReturn(List.of(employeeFunction, inactiveFunction));
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employeeFunction)).thenReturn(responseDTO);

        List<EmployeeFunctionResponseDTO> result = employeeFunctionService.getAllEmployeeFunctions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Gerente", result.get(0).getDescription());
    }

    @Test
    @DisplayName("Deve listar todas as funções de funcionário, incluindo inativas")
    void shouldListAllEmployeeFunctionsIncludingInactive() {
        EmployeeFunction inactiveFunction = EmployeeFunction.builder().id(2L).description("Ex-Gerente").active(false).build();
        EmployeeFunctionResponseDTO inactiveResponseDTO = EmployeeFunctionResponseDTO.builder().id(2L).description("Ex-Gerente").active(false).build();
        when(employeeFunctionRepository.findAll()).thenReturn(List.of(employeeFunction, inactiveFunction));
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employeeFunction)).thenReturn(responseDTO);
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(inactiveFunction)).thenReturn(inactiveResponseDTO);

        List<EmployeeFunctionResponseDTO> result = employeeFunctionService.getAllEmployeeFunctionsIncludingInactive();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Deve atualizar função de funcionário existente com sucesso")
    void shouldUpdateEmployeeFunctionSuccessfully() {
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        when(employeeFunctionRepository.save(any(EmployeeFunction.class))).thenReturn(employeeFunction);
        when(employeeFunctionMapper.toEmployeeFunctionResponseDTO(employeeFunction)).thenReturn(responseDTO);
        Optional<EmployeeFunctionResponseDTO> result = employeeFunctionService.updateEmployeeFunction(1L, requestDTO);
        assertTrue(result.isPresent());
        assertEquals("Gerente", result.get().getDescription());
    }

    @Test
    @DisplayName("Não deve atualizar função de funcionário se não for encontrada")
    void shouldNotUpdateEmployeeFunctionIfNotFound() {
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<EmployeeFunctionResponseDTO> result = employeeFunctionService.updateEmployeeFunction(99L, requestDTO);
        assertFalse(result.isPresent());
        verify(employeeFunctionRepository, never()).save(any(EmployeeFunction.class));
    }

    @Test
    @DisplayName("Deve inativar função de funcionário com sucesso")
    void shouldInactivateEmployeeFunctionSuccessfully() {
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        boolean result = employeeFunctionService.inactivateEmployeeFunction(1L);
        assertTrue(result);
        assertFalse(employeeFunction.getActive());
        verify(employeeFunctionRepository, times(1)).save(employeeFunction);
    }

    @Test
    @DisplayName("Não deve inativar função de funcionário se não for encontrada")
    void shouldNotInactivateEmployeeFunctionIfNotFound() {
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = employeeFunctionService.inactivateEmployeeFunction(99L);
        assertFalse(result);
        verify(employeeFunctionRepository, never()).save(any(EmployeeFunction.class));
    }

    @Test
    @DisplayName("Deve ativar função de funcionário com sucesso")
    void shouldActivateEmployeeFunctionSuccessfully() {
        employeeFunction.setActive(false);
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        boolean result = employeeFunctionService.activateEmployeeFunction(1L);
        assertTrue(result);
        assertTrue(employeeFunction.getActive());
        verify(employeeFunctionRepository, times(1)).save(employeeFunction);
    }

    @Test
    @DisplayName("Não deve ativar função de funcionário se não for encontrada")
    void shouldNotActivateEmployeeFunctionIfNotFound() {
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = employeeFunctionService.activateEmployeeFunction(99L);
        assertFalse(result);
        verify(employeeFunctionRepository, never()).save(any(EmployeeFunction.class));
    }
}

