package com.fiap.soat12.tc_group_7.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailClient {

    private final JavaMailSender mailSender;

    private final MailContentBuilder mailContentBuilder;

    public void sendMail(String recipient, String subject, String templateName, Map<String, Object> variables) throws MessagingException {

        String content = mailContentBuilder.build(templateName, variables);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        String username = null;
        if (mailSender instanceof JavaMailSenderImpl mailSenderImpl) {
            username = mailSenderImpl.getUsername();
        }

        if (username != null) {
            helper.setFrom(username);
        } else {
            throw new MessagingException("O nome de usuário do remetente do e-mail não pode ser nulo.");
        }

        helper.setTo(recipient);
        helper.setText(content, true);
        helper.setSubject(subject);

        mailSender.send(mimeMessage);
    }
}