package com.fiap.soat12.tc_group_7.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailClient {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired(required = false)
    private MailContentBuilder mailContentBuilder;

    public void sendMail(String recipient, String subject, String templateName, Map<String, Object> variables) throws Exception {
        try {

        	String content = mailContentBuilder.build(templateName, variables);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(((JavaMailSenderImpl) mailSender).getUsername());
            helper.setTo(recipient);
            helper.setText(content, true);
            helper.setSubject(subject);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new Exception("Falha ao enviar e-mail para " + recipient, e);
        }
    }
}