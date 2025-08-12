package com.fiap.soat12.tc_group_7.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fiap.soat12.tc_group_7.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequestFilter Unit Tests")
public class RequestFilterTest {

    @Mock
    private SessionToken sessionToken;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private PrintWriter printWriter;

    @InjectMocks
    private RequestFilter requestFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        ReflectionTestUtils.setField(requestFilter, "whiteList", new java.util.HashSet<>());
        ReflectionTestUtils.setField(requestFilter, "pathWhiteList", new java.util.HashSet<>());
    }

    @Test
    @DisplayName("Should pass request to the next filter if there is no Authorization header")
    void doFilterInternal_withNoAuthorizationHeader_shouldContinueChain() throws ServletException, IOException, JsonProcessingException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(securityContext, never()).setAuthentication(any());
    }

    @Test
    @DisplayName("Should pass request to the next filter if SecurityContext already has authentication")
    void doFilterInternal_withExistingAuthentication_shouldContinueChain() throws ServletException, IOException, JsonProcessingException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer a.b.c");
        when(securityContext.getAuthentication()).thenReturn(new UsernamePasswordAuthenticationToken("user", "pass"));

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain).doFilter(request, response);
        verify(securityContext, times(0)).setAuthentication(any());
    }

    @Test
    @DisplayName("Should handle generic exception and write an error response")
    void doFilterInternal_withGenericException_shouldWriteErrorResponse() throws Exception {
        // Arrange
        when(request.getHeader("Authorization")).thenThrow(new RuntimeException("Test Exception"));
        when(response.getWriter()).thenReturn(printWriter);

        // Act
        requestFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(printWriter).write(anyString());
        verify(response).setStatus(eq(400));
        verify(filterChain, never()).doFilter(any(), any());
    }
}