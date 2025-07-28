package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeFunctionRepository employeeFunctionRepository;

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponseDTO> getAllActiveEmployees() {
        return employeeRepository.findAll().stream()
                .filter(Employee::getActive)
                .map(employeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeResponseDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDTO);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        EmployeeFunction function = employeeFunctionRepository.findById(requestDTO.getEmployeeFunctionId())
                .orElseThrow(() -> new IllegalArgumentException("Função não encontrada"));
        Employee employee = employeeMapper.toEntity(requestDTO, function);
        employee.setCreatedAt(LocalDateTime.now());
        employee.setUpdatedAt(LocalDateTime.now());
        employee.setActive(true);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toResponseDTO(saved);
    }

    @Transactional
    public Optional<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        return employeeRepository.findById(id).map(existing -> {
            EmployeeFunction function = employeeFunctionRepository.findById(requestDTO.getEmployeeFunctionId())
                    .orElseThrow(() -> new IllegalArgumentException("Função não encontrada"));
            existing.setCpf(requestDTO.getCpf());
            existing.setName(requestDTO.getName());
            existing.setPassword(requestDTO.getPassword());
            existing.setPhone(requestDTO.getPhone());
            existing.setEmail(requestDTO.getEmail());
            existing.setActive(requestDTO.getActive());
            existing.setEmployeeFunction(function);
            existing.setUpdatedAt(LocalDateTime.now());
            Employee updated = employeeRepository.save(existing);
            return employeeMapper.toResponseDTO(updated);
        });
    }

    @Transactional
    public boolean inactivateEmployee(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setActive(false);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean activateEmployee(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setActive(true);
            employee.setUpdatedAt(LocalDateTime.now());
            employeeRepository.save(employee);
            return true;
        }).orElse(false);
    }
}
