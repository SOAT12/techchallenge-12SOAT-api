package com.fiap.soat12.tc_group_7.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest request;

    @Test
    @DisplayName("Should handle IllegalArgumentException and return a formatted ErrorResponseDTO with HTTP 400")
    void handleIllegalArgumentException_shouldReturnBadRequest() {
        // Arrange
        String errorMessage = "Invalid argument provided.";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);
        String requestUri = "uri=/api/test";

        when(request.getDescription(false)).thenReturn(requestUri);

        // Act
        ResponseEntity<ErrorResponseDTO> responseEntity = globalExceptionHandler.handleIllegalArgumentException(exception, request);
        ErrorResponseDTO responseBody = responseEntity.getBody();

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals(HttpStatus.BAD_REQUEST.getReasonPhrase(), responseBody.getTitle());
        assertEquals(errorMessage, responseBody.getDetail());
        assertEquals("/api/test", responseBody.getPath());
    }
}