package com.fiap.soat12.tc_group_7.cleanarch.usecase;

import com.fiap.soat12.tc_group_7.cleanarch.gateway.EmployeeGateway;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.CodeGeneratorPort;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.EncryptionPort;
import com.fiap.soat12.tc_group_7.cleanarch.interfaces.NotificationPort;
import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.exception.BadCredentialsException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class PasswordManagementUseCase {

    private final EmployeeGateway employeeGateway;
    private final EncryptionPort encryptionPort;
    private final CodeGeneratorPort codeGeneratorPort;
    private final NotificationPort notificationPort;

    private static final String EMPLOYEE_NOT_FOUND = "Funcionário não encontrado.";

    public void changePassword(Long employeeId, ChangePasswordRequestDTO request) throws Exception {
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new BadCredentialsException("A nova senha e a confirmação não são iguais.", null);
        }

        var employee = employeeGateway.findById(employeeId)
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND));

        boolean isValidOldPassword = encryptionPort.checkMatch(
                request.getOldPassword(),
                employee.getPassword()
        );

        if (!isValidOldPassword) {
            throw new BadCredentialsException("A senha antiga está incorreta.", null);
        }

        boolean newPasswordIsDifferent = !encryptionPort.checkMatch(
                request.getNewPassword(),
                employee.getPassword()
        );

        if (!newPasswordIsDifferent) {
            throw new BadCredentialsException("A nova senha não pode ser igual à antiga.", null);
        }

        employee.setPassword(encryptionPort.hash(request.getNewPassword()));
        employee.setTemporaryPassword("");
        employee.setPasswordValidity(null);
        employee.setUseTemporaryPassword(false);

        employeeGateway.save(employee);
    }

    public void forgotPassword(ForgotPasswordRequestDTO request) throws Exception {
        var employee = employeeGateway.findByCpf(request.getCpf())
                .orElseThrow(() -> new NotFoundException(EMPLOYEE_NOT_FOUND));

        String tempPassword = codeGeneratorPort.generateCode().toLowerCase();

        Map<String, Object> variables = new HashMap<>();
        variables.put("message", tempPassword);
        String subject = "Redefinição de Senha - Oficina";

        try {
            notificationPort.sendEmail(employee.getEmail(), subject, "mailTemplate", variables);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail de redefinição.", e);
        }

        employee.setTemporaryPassword(encryptionPort.hash(tempPassword));
        employee.setPasswordValidity(LocalDateTime.now().plusMinutes(600)); // 10 horas de validade
        employee.setUseTemporaryPassword(true);

        employeeGateway.save(employee);
    }
}