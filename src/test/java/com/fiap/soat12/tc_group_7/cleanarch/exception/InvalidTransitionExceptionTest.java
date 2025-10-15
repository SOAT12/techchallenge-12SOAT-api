package com.fiap.soat12.tc_group_7.cleanarch.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidTransitionExceptionTest {

    @Test
    void shouldCreateInvalidTransitionException() {
        String message = "Test Exception";
        InvalidTransitionException exception = new InvalidTransitionException(message);
        assertEquals(message, exception.getMessage());
    }
}
