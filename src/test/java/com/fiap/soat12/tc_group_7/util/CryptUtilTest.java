package com.fiap.soat12.tc_group_7.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CryptUtilTest {

    @Test
    @DisplayName("Should generate a valid MD5 hash for a given string")
    void md5_shouldGenerateValidHash() {
        // Arrange
        String value = "password123";

        // Act
        String hash = CryptUtil.md5(value);

        // Assert
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
        // A simple check to ensure the hash is not the original string
        assertTrue(hash.length() > 0 && !hash.equals(value));
    }

    @Test
    @DisplayName("Should encode a string using BCrypt and return a valid hash")
    void bcrypt_shouldEncodeString() {
        // Arrange
        String value = "password123";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Act
        String hash = CryptUtil.bcrypt(value);

        // Assert
        assertNotNull(hash);
        assertTrue(passwordEncoder.matches(value, hash));
        // A check to ensure a different hash is generated for the same input
        String hash2 = new BCryptPasswordEncoder().encode(value);
        assertNotEquals(hash, hash2);
    }
}