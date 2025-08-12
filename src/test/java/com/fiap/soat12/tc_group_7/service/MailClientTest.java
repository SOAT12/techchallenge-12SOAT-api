package com.fiap.soat12.tc_group_7.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailClientTest {

    @Mock
    private JavaMailSenderImpl mailSender;

    @Mock
    private MailContentBuilder mailContentBuilder;

    @InjectMocks
    private MailClient mailClient;

    private String recipient;
    private String subject;
    private String templateName;
    private Map<String, Object> variables;
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        recipient = "test@example.com";
        subject = "Test Subject";
        templateName = "mailTemplate";
        variables = new HashMap<>();

        // Create a real MimeMessage object for the test
        mimeMessage = new MimeMessage((jakarta.mail.Session) null);

        // Common mocking setup
        when(mailContentBuilder.build(eq(templateName), eq(variables))).thenReturn("Test Content");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    @DisplayName("Should send an email successfully when JavaMailSender is JavaMailSenderImpl and username is present")
    void sendMail_withJavaMailSenderImplAndUsername_shouldSendSuccessfully() throws MessagingException {
        // Arrange
        String username = "sender@example.com";
        when(mailSender.getUsername()).thenReturn(username);

        // Act
        mailClient.sendMail(recipient, subject, templateName, variables);

        // Assert
        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    @DisplayName("Should throw MessagingException when username is null, regardless of sender type")
    void sendMail_withNullUsername_shouldThrowException() {
        // Arrange
        when(mailSender.getUsername()).thenReturn(null);

        // Act & Assert
        MessagingException exception = assertThrows(MessagingException.class, () ->
                mailClient.sendMail(recipient, subject, templateName, variables)
        );

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, never()).send(any(MimeMessage.class));
        assertEquals("O nome de usuário do remetente do e-mail não pode ser nulo.", exception.getMessage());
    }
}