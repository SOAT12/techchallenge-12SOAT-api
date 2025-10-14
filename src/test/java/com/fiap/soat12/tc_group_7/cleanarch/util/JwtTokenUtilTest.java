package com.fiap.soat12.tc_group_7.cleanarch.util;

import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private String token;
    private final String subject = "teste@email.com";
    private final Map<String, Object> claims = new HashMap<>();

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        claims.put("role", "ADMIN");
        token = jwtTokenUtil.generateToken(claims, subject);
    }

    @Test
    @DisplayName("Deve gerar um token válido com sucesso")
    void shouldGenerateValidToken() {
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve extrair o subject do token corretamente")
    void shouldExtractSubjectFromToken() throws Exception {
        String extractedSubject = jwtTokenUtil.getSubject(token);
        assertEquals(subject, extractedSubject);
    }

    @Test
    @DisplayName("Deve extrair a data de expiração do token corretamente")
    void shouldExtractExpirationDateFromToken() throws Exception {
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    @DisplayName("Deve decodificar um token com claims corretamente")
    void shouldGetAllClaimsFromToken() throws Exception {
        Map<String, Object> extractedClaims = jwtTokenUtil.getAllClaimsFromToken(token);
        assertEquals("ADMIN", extractedClaims.get("role"));
    }

    @Test
    @DisplayName("Deve lançar uma exceção de assinatura quando a chave secreta for inválida")
    void shouldThrowSignatureExceptionWhenSecretIsInvalid() {
        // Geração de um token com uma chave secreta diferente para testar a validação de assinatura
        String invalidSecret = new String(java.util.Base64.getEncoder().encode("diferentesecretkeydiferentesecretkey".getBytes()));

        JwtTokenUtil invalidJwtUtil = new JwtTokenUtil();
        try {
            java.lang.reflect.Field field = invalidJwtUtil.getClass().getDeclaredField("secret");
            field.setAccessible(true);
            field.set(invalidJwtUtil, invalidSecret);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Falha ao modificar o campo de segredo.");
        }

        String invalidToken = invalidJwtUtil.generateToken(Collections.emptyMap(), "invaliduser@email.com");

        // Tentamos usar o token gerado com a chave original (jwtTokenUtil)
        assertThrows(SignatureException.class, () -> jwtTokenUtil.getAllClaimsFromToken(invalidToken));
    }

    @Test
    @DisplayName("Deve renovar a chave secreta com sucesso")
    void shouldRenewSecretSuccessfully() throws Exception {

        jwtTokenUtil.renewSecret();

        assertThrows(SignatureException.class, () -> jwtTokenUtil.getAllClaimsFromToken(token));

        String newToken = jwtTokenUtil.generateToken(claims, subject);

        assertDoesNotThrow(() -> jwtTokenUtil.getAllClaimsFromToken(newToken));
    }
}