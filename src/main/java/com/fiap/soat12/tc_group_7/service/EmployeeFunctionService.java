package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeFunctionResponseDTO;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.mapper.EmployeeFunctionMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeFunctionService {
    private final EmployeeFunctionRepository employeeFunctionRepository;
    private final EmployeeFunctionMapper employeeFunctionMapper;

    @Autowired
    public EmployeeFunctionService(EmployeeFunctionRepository employeeFunctionRepository, EmployeeFunctionMapper employeeFunctionMapper) {
        this.employeeFunctionRepository = employeeFunctionRepository;
        this.employeeFunctionMapper = employeeFunctionMapper;
    }

    public EmployeeFunctionResponseDTO createEmployeeFunction(EmployeeFunctionRequestDTO requestDTO) {
        EmployeeFunction entity = employeeFunctionMapper.toEntity(requestDTO);
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        EmployeeFunction saved = employeeFunctionRepository.save(entity);
        return employeeFunctionMapper.toResponseDTO(saved);
    }

    public Optional<EmployeeFunctionResponseDTO> getEmployeeFunctionById(Long id) {
        return employeeFunctionRepository.findById(id)
                .map(employeeFunctionMapper::toResponseDTO);
    }

    public List<EmployeeFunctionResponseDTO> getAllEmployeeFunctions() {
        return employeeFunctionRepository.findAll().stream()
                .map(employeeFunctionMapper::toResponseDTO)
                .collect(java.util.stream.Collectors.toList());
    }

    public Optional<EmployeeFunctionResponseDTO> updateEmployeeFunction(Long id, EmployeeFunctionRequestDTO requestDTO) {
        return employeeFunctionRepository.findById(id).map(existing -> {
            existing.setDescription(requestDTO.getDescription());
            existing.setUpdatedAt(java.time.LocalDateTime.now());
            EmployeeFunction updated = employeeFunctionRepository.save(existing);
            return employeeFunctionMapper.toResponseDTO(updated);
        });
    }

    public boolean deleteEmployeeFunction(Long id) {
        if (!employeeFunctionRepository.existsById(id)) return false;
        employeeFunctionRepository.deleteById(id);
        return true;
    }
}
