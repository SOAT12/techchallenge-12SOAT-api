package com.fiap.soat12.tc_group_7.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.util.CodeGenerator;
import com.fiap.soat12.tc_group_7.util.CryptUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeFunctionRepository employeeFunctionRepository;

    public List<EmployeeResponseDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployeeResponseDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeResponseDTO> getAllActiveEmployees() {
        return employeeRepository.findAllByActiveTrue().stream()
                .map(employeeMapper::toEmployeeResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<EmployeeResponseDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toEmployeeResponseDTO);
    }

    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO requestDTO) {
        EmployeeFunction function = employeeFunctionRepository.findById(requestDTO.getEmployeeFunctionId())
                .orElseThrow(() -> new IllegalArgumentException("Função não encontrada"));
        Employee employee = employeeMapper.toEmployee(requestDTO, function);
        employee.setCreatedAt(new Date());
        employee.setUpdatedAt(new Date());
        employee.setActive(true);
        Employee saved = employeeRepository.save(employee);
        return employeeMapper.toEmployeeResponseDTO(saved);
    }

    @Transactional
    public Optional<EmployeeResponseDTO> updateEmployee(Long id, EmployeeRequestDTO requestDTO) {
        return employeeRepository.findById(id).map(existing -> {
            EmployeeFunction function = employeeFunctionRepository.findById(requestDTO.getEmployeeFunctionId())
                    .orElseThrow(() -> new IllegalArgumentException("Função não encontrada"));
            existing.setCpf(requestDTO.getCpf());
            existing.setName(requestDTO.getName());
            existing.setPhone(requestDTO.getPhone());
            existing.setEmail(requestDTO.getEmail());
            existing.setActive(requestDTO.getActive());
            existing.setEmployeeFunction(function);
            existing.setUpdatedAt(new Date());
            Employee updated = employeeRepository.save(existing);
            return employeeMapper.toEmployeeResponseDTO(updated);
        });
    }

    @Transactional
    public boolean inactivateEmployee(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setActive(false);
            employee.setUpdatedAt(new Date());
            employee.setUpdatedAt(new Date());

            employeeRepository.save(employee);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean activateEmployee(Long id) {
        return employeeRepository.findById(id).map(employee -> {
            employee.setActive(true);
            employee.setUpdatedAt(new Date());
            employeeRepository.save(employee);
            return true;
        }).orElse(false);
    }
    
	public void changePassword(Long id, ChangePasswordRequestDTO changePassword) throws Exception {

		if (!changePassword.getNewPassword().equals(changePassword.getConfirmationPassword())) {
			throw new BadCredentialsException("A nova senha e a confirmação não são iguais.");
		}

		Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Funcionario nao encontrado"));

		boolean isValid = new BCryptPasswordEncoder().matches(changePassword.getOldPassword(),
				employee.getPassword());

		if (!isValid) {
			throw new BadCredentialsException("A senha antiga está incorreta.");
		}
		
		boolean newPasswordIsDiferent = !new BCryptPasswordEncoder().matches(
	                changePassword.getNewPassword(),
	                employee.getPassword());

        if (!newPasswordIsDiferent) {
            throw new BadCredentialsException("A nova senha nao pode ser igual a antiga.");
        }

		employee.setPassword(CryptUtil.bcrypt(changePassword.getNewPassword()));

		employeeRepository.save(employee);
	}
	
	public void forgotPassword(ForgotPasswordRequestDTO forgotPassword) throws Exception {

		String tempPassword = CodeGenerator.generateCode().toLowerCase();

		Employee employee = employeeRepository.findByCpf(forgotPassword.getCpf())
				.orElseThrow(() -> new UsernameNotFoundException("FALHA NA IDENTIFICAÇÃO: " + forgotPassword.getCpf()));
		String recipient = employee.getCpf();
		String message = tempPassword;

		mailClient.prepareAndSend(recipient, message);

		employee.setTemporaryPassword(CryptUtil.bcrypt(tempPassword));
		employee.setPasswordValidity(DateUtils.toLocalDateTime(DateUtils.getCurrentDate()));
		employee.setUseTemporaryPassword(true);

		employeeRepository.save(employee);
	}
}
