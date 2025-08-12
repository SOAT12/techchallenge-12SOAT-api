package com.fiap.soat12.tc_group_7.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @InjectMocks
    private JwtAuthenticationEntryPoint entryPoint;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @Mock
    private PrintWriter writer;

    @Test
    @DisplayName("Deve retornar status 401 e um JSON com a mensagem de erro de autenticação")
    void commence_shouldReturnUnauthorizedResponse() throws IOException {
        // Arrange
        String errorMessage = "Acesso negado: Credenciais inválidas.";
        when(authException.getMessage()).thenReturn(errorMessage);
        when(response.getWriter()).thenReturn(writer);

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Configuração do ObjectMapper para simular a saída esperada
        ObjectMapper objectMapper = new ObjectMapper();
        ApiError expectedError = new ApiError(HttpStatus.UNAUTHORIZED, errorMessage, authException);
        String expectedJson = objectMapper.writeValueAsString(expectedError);

        verify(writer).write(expectedJson);
    }

    @Test
    @DisplayName("Deve lançar exceção de I/O se a escrita da resposta falhar")
    void commence_withIoException_shouldThrowException() throws IOException {
        // Arrange
        when(response.getWriter()).thenThrow(new IOException("Erro ao escrever a resposta."));

        // A exceção deve ser lançada depois que o status for definido
        // A linha de mock desnecessária foi removida.

        // Act & Assert
        assertThrows(IOException.class, () -> entryPoint.commence(request, response, authException));

        // As verificações agora estão corretas, pois setStatus é chamado antes do erro
        verify(response).setContentType("application/json;charset=UTF-8");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
