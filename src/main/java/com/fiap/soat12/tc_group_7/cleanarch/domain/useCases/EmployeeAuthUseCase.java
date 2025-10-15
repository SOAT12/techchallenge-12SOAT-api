package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee;
import com.fiap.soat12.tc_group_7.cleanarch.domain.port.EncryptionPort;
import com.fiap.soat12.tc_group_7.cleanarch.domain.port.TokenServicePort;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeGateway;
import com.fiap.soat12.tc_group_7.cleanarch.util.DateUtils;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public class EmployeeAuthUseCase {

    private final EmployeeGateway employeeGateway;
    private final EncryptionPort encryptionPort;
    private final TokenServicePort tokenServicePort;

    private static final String FALHA_IDENTIFICACAO_MSG = "FALHA NA IDENTIFICAÇÃO: ";
    private static final long TEMP_PWD_VALIDITY_MINUTES = 600; // 10 horas
    private static final String ROLE_PREFIX = "ROLE_";

    public LoginResponseDTO auth(LoginRequestDTO requestDTO) throws Exception {
        Employee employee = employeeGateway.findByCpf(requestDTO.getCpf())
                .orElseThrow(() -> new NotFoundException(FALHA_IDENTIFICACAO_MSG + requestDTO.getCpf()));

        if (encryptionPort.checkMatch(requestDTO.getPassword(), employee.getPassword())) {
            if (Boolean.TRUE.equals(employee.getUseTemporaryPassword())) {
                resetTemporaryPasswordFlags(employee);
            }
            return generateTokenResponse(employee);
        }

        if (Boolean.TRUE.equals(employee.getUseTemporaryPassword())) {
            if (isValidTemporaryPassword(employee)) {
                if (encryptionPort.checkMatch(requestDTO.getPassword(), employee.getTemporaryPassword())) {
                    updatePasswordAfterTemporaryLogin(employee, requestDTO.getPassword());
                    return generateTokenResponse(employee);
                }
            } else {
                resetTemporaryPasswordFlags(employee);
            }
        }
        throw new BadCredentialsException("INVALID_CREDENTIALS", null);
    }

    private boolean isValidTemporaryPassword(Employee employee) {
        if (employee.getPasswordValidity() == null) return false;
        Date passwordValidity = DateUtils.toDate(employee.getPasswordValidity());

        return true;
    }

    private void resetTemporaryPasswordFlags(Employee employee) {
        employee.setTemporaryPassword("");
        employee.setPasswordValidity(null);
        employee.setUseTemporaryPassword(false);
        employeeGateway.save(employee);
    }

    private void updatePasswordAfterTemporaryLogin(Employee employee, String newPassword) {
        employee.setPassword(encryptionPort.hash(newPassword));
        resetTemporaryPasswordFlags(employee);
    }

    private LoginResponseDTO generateTokenResponse(Employee employee) {
        String employeeRoleDescription = employee.getEmployeeFunction().getDescription();
        String prefixedRole = ROLE_PREFIX + employeeRoleDescription.toUpperCase().trim();
        List<String> authorities = List.of(prefixedRole);

        return tokenServicePort.generateToken(employee, authorities);
    }
}