package com.fiap.soat12.tc_group_7.cleanarch.exception;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Component
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final MeterRegistry meterRegistry;

    public GlobalExceptionHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    // 1. FALHA DE FLUXO (Ex: Tentar finalizar uma OS que não está em execução)
    @ExceptionHandler(InvalidTransitionException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidTransitionException(InvalidTransitionException ex, WebRequest request) {
        // Tag 'reason=invalid_status' identifica erro de regra de negócio da OS
        meterRegistry.counter("business.service_order.error", "reason", "invalid_status").increment();

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    // 2. FALHA DE INFRAESTRUTURA NA OS (Ex: Erro ao enviar e-mail de conclusão)
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponseDTO> handleMessagingException(MessagingException ex, WebRequest request) {

        meterRegistry.counter("business.service_order.error", "reason", "notification_failed").increment();

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Falha ao enviar notificação da Ordem de Serviço.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 3. FALHA DE DADOS (Ex: Tentar criar OS para cliente/veículo que não existe)
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(NotFoundException ex, WebRequest request) {
        // Tag 'reason=data_not_found'
        meterRegistry.counter("business.service_order.error", "reason", "data_not_found").increment();

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    // 4. FALHA GENÉRICA CRÍTICA (NullPointer, Banco caiu, etc)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(Exception ex, WebRequest request) {
        // Tag 'reason=crash' - ISSO É UMA FALHA GRAVE DE PROCESSAMENTO
        meterRegistry.counter("business.service_order.error", "reason", "crash").increment();

        ex.printStackTrace(); // Logar o erro para debug

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Erro interno no processamento da requisição.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

}
