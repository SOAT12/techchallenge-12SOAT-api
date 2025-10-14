package com.fiap.soat12.tc_group_7.cleanarch.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class UUIDGeneratorUtilTest {

    @Test
    @DisplayName("Deve garantir que apenas uma instância seja criada (Singleton)")
    void shouldEnsureSingleInstance() {
        // Obter duas instâncias da classe
        UUIDGeneratorUtil instance1 = UUIDGeneratorUtil.getInstance();
        UUIDGeneratorUtil instance2 = UUIDGeneratorUtil.getInstance();

        // Verificar se as duas referências apontam para o mesmo objeto
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("Deve gerar um UUID válido")
    void shouldGenerateValidUUID() {
        UUIDGeneratorUtil generator = UUIDGeneratorUtil.getInstance();
        String uuid = generator.next();

        // Verificar se o UUID não é nulo e segue o formato padrão (8-4-4-4-12)
        assertNotNull(uuid);
        String uuidRegex = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
        assertTrue(Pattern.matches(uuidRegex, uuid));
    }

    @Test
    @DisplayName("Deve garantir a geração de UUIDs únicos em um ambiente de múltiplas threads")
    void shouldGenerateUniqueUUIDsInMultithreadedEnvironment() throws InterruptedException {
        int numberOfThreads = 10;
        int uuidsPerThread = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        Set<String> generatedUUIDs = new HashSet<>();

        // Usamos um bloco sincronizado para garantir que as adições ao conjunto sejam thread-safe
        Runnable uuidGeneratorTask = () -> {
            UUIDGeneratorUtil generator = UUIDGeneratorUtil.getInstance();
            for (int i = 0; i < uuidsPerThread; i++) {
                String uuid = generator.next();
                synchronized (generatedUUIDs) {
                    generatedUUIDs.add(uuid);
                }
            }
        };

        // Submeter as tarefas para o executor
        for (int i = 0; i < numberOfThreads; i++) {
            executor.submit(uuidGeneratorTask);
        }

        // Aguardar a conclusão de todas as threads
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // O número de UUIDs gerados deve ser igual ao número total de UUIDs esperados,
        // o que prova que não houve duplicatas.
        int totalExpectedUUIDs = numberOfThreads * uuidsPerThread;
        assertEquals(totalExpectedUUIDs, generatedUUIDs.size());
    }
}