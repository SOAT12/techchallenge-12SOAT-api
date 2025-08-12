package com.fiap.soat12.tc_group_7.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApiErrorTest {

    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    private final String errorMessage = "Mensagem de erro de teste";
    private final Throwable exception = new RuntimeException("Detalhe do erro");

    @Test
    @DisplayName("Deve inicializar ApiError com o construtor padrão, definindo apenas o timestamp")
    void testDefaultConstructor() {
        // Arrange & Act
        ApiError apiError = new ApiError();

        // Assert
        assertNotNull(apiError.getTimestamp());
        assertNull(apiError.getStatus());
        assertNull(apiError.getMessage());
        assertNull(apiError.getDebugMessage());
        assertNull(apiError.getSubErrors());
    }

    @Test
    @DisplayName("Deve inicializar ApiError com o construtor que aceita HttpStatus")
    void testHttpStatusConstructor() {
        ApiError apiError = new ApiError(httpStatus);

        assertNotNull(apiError.getTimestamp());
        assertEquals(httpStatus, apiError.getStatus());
        assertNull(apiError.getMessage());
    }

    @Test
    @DisplayName("Deve inicializar ApiError com o construtor que aceita HttpStatus e Throwable")
    void testHttpStatusAndThrowableConstructor() {
        ApiError apiError = new ApiError(httpStatus, exception);

        assertNotNull(apiError.getTimestamp());
        assertEquals(httpStatus, apiError.getStatus());
        assertEquals("Unexpected error", apiError.getMessage());
        assertEquals(exception.getClass().getName(), apiError.getDebugMessage());
    }

    @Test
    @DisplayName("Deve inicializar ApiError com o construtor que aceita HttpStatus, mensagem e Throwable")
    void testHttpStatusMessageAndThrowableConstructor() {
        ApiError apiError = new ApiError(httpStatus, errorMessage, exception);

        assertNotNull(apiError.getTimestamp());
        assertEquals(httpStatus, apiError.getStatus());
        assertEquals(errorMessage, apiError.getMessage());
        assertEquals(exception.getClass().getName(), apiError.getDebugMessage());
    }

    @Test
    @DisplayName("Deve inicializar ApiError usando o builder com todos os campos")
    void testBuilder() {
        String debugMessage = "Debug de teste";
        List<ApiSubError> subErrors = List.of(new ApiValidationError());
        LocalDateTime now = LocalDateTime.now();

        ApiError apiError = ApiError.builder()
                .status(httpStatus)
                .timestamp(now)
                .message(errorMessage)
                .debugMessage(debugMessage)
                .subErrors(subErrors)
                .build();

        assertEquals(httpStatus, apiError.getStatus());
        assertEquals(now, apiError.getTimestamp());
        assertEquals(errorMessage, apiError.getMessage());
        assertEquals(debugMessage, apiError.getDebugMessage());
        assertEquals(subErrors, apiError.getSubErrors());
    }

    static class ApiValidationError extends ApiSubError {
    }
}