package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeFunctionRepository employeeFunctionRepository;
    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeFunction employeeFunction;
    private EmployeeRequestDTO requestDTO;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        employeeFunction = EmployeeFunction.builder()
                .id(1L)
                .description("Gerente")
                .build();
        employee = Employee.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .employeeFunction(employeeFunction)
                .password("senha123")
                .build();
        requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();
        responseDTO = EmployeeResponseDTO.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(null)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os funcionários")
    void shouldListAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        List<EmployeeResponseDTO> result = employeeService.getAllEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Maria Souza", result.get(0).getName());
    }

    @Test
    @DisplayName("Deve listar todos os funcionários ativos")
    void shouldListAllActiveEmployees() {
        when(employeeRepository.findAllByActiveTrue()).thenReturn(List.of(employee));
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        List<EmployeeResponseDTO> result = employeeService.getAllActiveEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }

    @Test
    @DisplayName("Deve retornar funcionário por ID")
    void shouldReturnEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        Optional<EmployeeResponseDTO> result = employeeService.getEmployeeById(1L);
        assertTrue(result.isPresent());
        assertEquals("Maria Souza", result.get().getName());
    }

    @Test
    @DisplayName("Deve criar funcionário com sucesso")
    void shouldCreateEmployeeSuccessfully() {
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        when(employeeMapper.toEmployee(requestDTO, employeeFunction)).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        EmployeeResponseDTO result = employeeService.createEmployee(requestDTO);
        assertNotNull(result);
        assertEquals("Maria Souza", result.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar funcionário com função inexistente")
    void shouldThrowExceptionWhenCreatingEmployeeWithNonExistentFunction() {
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        requestDTO.setEmployeeFunctionId(99L);
        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(requestDTO));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve atualizar funcionário existente com sucesso")
    void shouldUpdateEmployeeSuccessfully() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        Optional<EmployeeResponseDTO> result = employeeService.updateEmployee(1L, requestDTO);
        assertTrue(result.isPresent());
        assertEquals("Maria Souza", result.get().getName());
    }

    @Test
    @DisplayName("Não deve atualizar funcionário se não for encontrado")
    void shouldNotUpdateEmployeeIfNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<EmployeeResponseDTO> result = employeeService.updateEmployee(99L, requestDTO);
        assertFalse(result.isPresent());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar funcionário com função inexistente")
    void shouldThrowExceptionWhenUpdatingEmployeeWithNonExistentFunction() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        requestDTO.setEmployeeFunctionId(99L);
        assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(1L, requestDTO));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve inativar funcionário com sucesso")
    void shouldInactivateEmployeeSuccessfully() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        boolean result = employeeService.inactivateEmployee(1L);
        assertTrue(result);
        assertFalse(employee.getActive());
    }

    @Test
    @DisplayName("Não deve inativar funcionário se não for encontrado")
    void shouldNotInactivateEmployeeIfNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = employeeService.inactivateEmployee(99L);
        assertFalse(result);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve ativar funcionário com sucesso")
    void shouldActivateEmployeeSuccessfully() {
        employee.setActive(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        boolean result = employeeService.activateEmployee(1L);
        assertTrue(result);
        assertTrue(employee.getActive());
    }

    @Test
    @DisplayName("Não deve ativar funcionário se não for encontrado")
    void shouldNotActivateEmployeeIfNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = employeeService.activateEmployee(99L);
        assertFalse(result);
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}

