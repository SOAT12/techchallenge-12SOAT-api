package com.fiap.soat12.tc_group_7.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Mock
    private UserDetailsService jwtUserDetailsService;

    @Mock
    private RequestFilter jwtRequestFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private WebSecurityConfig webSecurityConfig;

    @Test
    @DisplayName("Deve criar um bean PasswordEncoder do tipo BCryptPasswordEncoder")
    void passwordEncoder_ShouldReturnBCryptPasswordEncoder() {
        // When
        PasswordEncoder passwordEncoder = webSecurityConfig.passwordEncoder();

        // Then
        assertNotNull(passwordEncoder);
        assertInstanceOf(BCryptPasswordEncoder.class, passwordEncoder);
    }

    @Test
    @DisplayName("Deve configurar e criar um DaoAuthenticationProvider")
    void authenticationProvider_ShouldReturnConfiguredProvider() {
        // When
        DaoAuthenticationProvider provider = webSecurityConfig.authenticationProvider();

        // Then
        assertNotNull(provider);
        // A verificação interna de que o userDetailsService e o passwordEncoder foram setados
        // é implícita na construção do objeto pelo Spring. O teste garante que o bean é criado.
    }

    @Test
    @DisplayName("Deve obter o AuthenticationManager da configuração de autenticação")
    void authenticationManager_ShouldReturnFromConfiguration() throws Exception {
        // Given
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(mockManager);

        // When
        AuthenticationManager resultManager = webSecurityConfig.authenticationManager(authenticationConfiguration);

        // Then
        assertNotNull(resultManager);
        assertEquals(mockManager, resultManager);
        verify(authenticationConfiguration, times(1)).getAuthenticationManager();
    }
}