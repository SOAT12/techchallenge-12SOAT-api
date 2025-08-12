package com.fiap.soat12.tc_group_7.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTokenTest {

    private SessionToken sessionToken;

    // Configuração inicial para cada teste
    @BeforeEach
    void setUp() {
        sessionToken = new SessionToken();
    }

    @Test
    void testAddTransactionAndSession_shouldAddCorrectly() {
        // Cenário
        String sessionId = "sessao123";
        String transactionId = "transacao456";

        // Ação
        sessionToken.addTransactionAndSession(sessionId, transactionId);

        // Verificação
        assertEquals(transactionId, sessionToken.getSessions().get(sessionId), "O transactionId deve ser mapeado para o sessionId");
        assertEquals(sessionId, sessionToken.getTransactions().get(transactionId), "O sessionId deve ser mapeado para o transactionId");
        assertEquals(1, sessionToken.getSessions().size(), "Deve haver 1 sessão no mapa");
        assertEquals(1, sessionToken.getTransactions().size(), "Deve haver 1 transação no mapa");
    }

    @Test
    void testRemoveTransactionAndSession_shouldRemoveCorrectly() {
        // Cenário
        String sessionId = "sessao123";
        String transactionId = "transacao456";
        sessionToken.addTransactionAndSession(sessionId, transactionId);

        // Ação
        sessionToken.removeTransactionAndSession(sessionId);

        // Verificação
        assertFalse(sessionToken.getSessions().containsKey(sessionId), "O sessionId deve ser removido do mapa de sessões");
        assertFalse(sessionToken.getTransactions().containsKey(transactionId), "O transactionId deve ser removido do mapa de transações");
        assertEquals(0, sessionToken.getSessions().size(), "O mapa de sessões deve estar vazio");
        assertEquals(0, sessionToken.getTransactions().size(), "O mapa de transações deve estar vazio");
    }

    @Test
    void testRemoveTransactionAndSession_whenSessionNotFound_shouldNotThrowException() {
        // Cenário
        String sessionId = "sessaoInexistente";

        // Ação e Verificação (o método não deve lançar exceção)
        assertDoesNotThrow(() -> sessionToken.removeTransactionAndSession(sessionId),
                "Tentar remover uma sessão inexistente não deve lançar uma exceção");
    }

    @Test
    void testGetSessionId_shouldReturnCorrectId() {
        // Cenário
        String sessionId = "sessao123";
        String transactionId = "transacao456";
        sessionToken.addTransactionAndSession(sessionId, transactionId);

        // Ação
        String retrievedSessionId = sessionToken.GetSessionId(transactionId);

        // Verificação
        assertEquals(sessionId, retrievedSessionId, "O sessionId retornado deve ser o correto");
    }

    @Test
    void testGetSessionId_whenTransactionNotFound_shouldReturnNull() {
        // Cenário
        String transactionId = "transacaoInexistente";

        // Ação
        String retrievedSessionId = sessionToken.GetSessionId(transactionId);

        // Verificação
        assertNull(retrievedSessionId, "Deve retornar null para um transactionId inexistente");
    }
}