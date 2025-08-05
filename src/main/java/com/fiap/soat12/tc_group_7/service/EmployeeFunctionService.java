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
import java.util.stream.Collectors;

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
        EmployeeFunction entity = employeeFunctionMapper.toEmployeeFunction(requestDTO);
        entity.setActive(true);
        EmployeeFunction saved = employeeFunctionRepository.save(entity);
        return employeeFunctionMapper.toEmployeeFunctionResponseDTO(saved);
    }

    public Optional<EmployeeFunctionResponseDTO> getEmployeeFunctionById(Long id) {
        return employeeFunctionRepository.findById(id)
                .map(employeeFunctionMapper::toEmployeeFunctionResponseDTO);
    }

    public List<EmployeeFunctionResponseDTO> getAllEmployeeFunctions() {
        return employeeFunctionRepository.findAll().stream()
                .filter(EmployeeFunction::getActive)
                .map(employeeFunctionMapper::toEmployeeFunctionResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeFunctionResponseDTO> getAllEmployeeFunctionsIncludingInactive() {
        return employeeFunctionRepository.findAll().stream()
                .map(employeeFunctionMapper::toEmployeeFunctionResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeFunctionResponseDTO> updateEmployeeFunction(Long id, EmployeeFunctionRequestDTO requestDTO) {
        return employeeFunctionRepository.findById(id).map(existing -> {
            existing.setDescription(requestDTO.getDescription());
            existing.setActive(requestDTO.getActive());
            EmployeeFunction updated = employeeFunctionRepository.save(existing);
            return employeeFunctionMapper.toEmployeeFunctionResponseDTO(updated);
        });
    }

    public boolean inactivateEmployeeFunction(Long id) {
        return employeeFunctionRepository.findById(id).map(employeeFunction -> {
            employeeFunction.setActive(false);
            employeeFunctionRepository.save(employeeFunction);
            return true;
        }).orElse(false);
    }

    public boolean activateEmployeeFunction(Long id) {
        return employeeFunctionRepository.findById(id).map(employeeFunction -> {
            employeeFunction.setActive(true);
            employeeFunctionRepository.save(employeeFunction);
            return true;
        }).orElse(false);
    }
}
