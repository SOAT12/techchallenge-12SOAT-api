package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.adapter;

import com.fiap.soat12.tc_group_7.cleanarch.domain.port.NotificationPort;
import com.fiap.soat12.tc_group_7.service.MailClient;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class MailClientAdapter implements NotificationPort {

    private final MailClient mailClient;

    @Override
    public void sendEmail(String recipient, String subject, String templateName, Map<String, Object> variables) throws MessagingException {
        mailClient.sendMail(recipient, subject, templateName, variables);
    }
}