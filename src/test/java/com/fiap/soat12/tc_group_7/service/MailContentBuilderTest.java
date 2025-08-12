package com.fiap.soat12.tc_group_7.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MailContentBuilderTest {

    @Mock
    private TemplateEngine templateEngine;

    @InjectMocks
    private MailContentBuilder mailContentBuilder;

    @Test
    @DisplayName("Should build email content successfully with a template and variables")
    void build_withValidData_shouldReturnProcessedContent() {
        // Arrange
        String templateName = "templateName";
        Map<String, Object> variables = new HashMap<>();
        variables.put("key1", "value1");
        variables.put("key2", "value2");
        String expectedContent = "<html><body><h1>Processed Content</h1></body></html>";

        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn(expectedContent);

        // Act
        String actualContent = mailContentBuilder.build(templateName, variables);

        // Assert
        assertNotNull(actualContent);
        assertEquals(expectedContent, actualContent);
    }

    @Test
    @DisplayName("Should return empty string if the template engine processes to an empty result")
    void build_withEmptyTemplate_shouldReturnEmptyString() {
        // Arrange
        String templateName = "emptyTemplate";
        Map<String, Object> variables = Collections.emptyMap();

        when(templateEngine.process(eq(templateName), any(Context.class))).thenReturn("");

        // Act
        String actualContent = mailContentBuilder.build(templateName, variables);

        // Assert
        assertNotNull(actualContent);
        assertEquals("", actualContent);
    }
}