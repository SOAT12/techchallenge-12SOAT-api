package com.fiap.soat12.tc_group_7.cleanarch.interfaces;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface NotificationPort {

    void sendEmail(String recipient, String subject, String templateName, Map<String, Object> variables) throws MessagingException;
}