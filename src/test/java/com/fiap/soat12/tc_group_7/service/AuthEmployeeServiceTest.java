package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.cleanarch.exception.BadCredentialsException;
import com.fiap.soat12.tc_group_7.cleanarch.util.JwtTokenUtil;
import com.fiap.soat12.tc_group_7.cleanarch.util.UUIDGeneratorUtil;
import com.fiap.soat12.tc_group_7.config.SessionToken;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthEmployeeServiceTest {

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private SessionToken sessionToken;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthEmployeeService authEmployeeService;

    private LoginRequestDTO loginRequestDTO;
    private Employee employee;

    @BeforeEach
    void setUp() {
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setCpf("12345678900");
        loginRequestDTO.setPassword("senha123");

        employee = new Employee();
        employee.setCpf("12345678900");
        employee.setPassword(new BCryptPasswordEncoder().encode("senha123"));
        employee.setUseTemporaryPassword(false);

        // Mock para o contexto de segurança
        SecurityContextHolder.setContext(securityContext);
        ReflectionTestUtils.setField(authEmployeeService, "authenticationManager", authenticationManager);
        ReflectionTestUtils.setField(authEmployeeService, "jwtTokenUtil", jwtTokenUtil);
        ReflectionTestUtils.setField(authEmployeeService, "sessionToken", sessionToken);
    }

    @Test
    void testAuth_shouldAuthenticateWithCorrectPasswordAndReturnToken() throws Exception {
        // Mock do comportamento do AuthenticationManager e do SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getCpf(), loginRequestDTO.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("GESTOR"))
        );
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock para o UUIDGenerator e JwtTokenUtil
        String mockUUID = UUID.randomUUID().toString();
        try (MockedStatic<UUIDGeneratorUtil> mockedUUIDUtil = mockStatic(UUIDGeneratorUtil.class)) {
            UUIDGeneratorUtil uuidGeneratorUtil = mock(UUIDGeneratorUtil.class);
            when(uuidGeneratorUtil.next()).thenReturn(mockUUID);
            mockedUUIDUtil.when(UUIDGeneratorUtil::getInstance).thenReturn(uuidGeneratorUtil);

            when(jwtTokenUtil.generateToken(anyMap(), eq(mockUUID))).thenReturn("mockedToken");

            // Ação
            LoginResponseDTO responseDTO = authEmployeeService.auth(loginRequestDTO);

            // Verificação
            assertNotNull(responseDTO);
            assertEquals("mockedToken", responseDTO.getToken());
            verify(sessionToken, times(1)).addTransactionAndSession(loginRequestDTO.getCpf(), mockUUID);
            verify(sessionToken, times(1)).removeTransactionAndSession(loginRequestDTO.getCpf());
            verify(employeeService, never()).authTemporaryPassword(any());
            verify(employeeService, never()).authenticatedTemporaryPassword(any(), anyBoolean());
            verify(employeeService, never()).authenticatedOldPassword(any());
        }
    }

    @Test
    void testAuth_withBadCredentials_shouldTryTemporaryPasswordFlow() throws Exception {
        // Mock do primeiro authenticate falhando e do segundo com sucesso
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("INVALID_CREDENTIALS"))
                .thenReturn(new UsernamePasswordAuthenticationToken(loginRequestDTO.getCpf(), loginRequestDTO.getPassword()));

        // Mock do SecurityContext
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getCpf(), loginRequestDTO.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("GESTOR"))
        );
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Mock do UUIDGenerator e JwtTokenUtil
        String mockUUID = UUID.randomUUID().toString();
        try (MockedStatic<UUIDGeneratorUtil> mockedUUIDUtil = mockStatic(UUIDGeneratorUtil.class)) {
            UUIDGeneratorUtil uuidGeneratorUtil = mock(UUIDGeneratorUtil.class);
            when(uuidGeneratorUtil.next()).thenReturn(mockUUID);
            mockedUUIDUtil.when(UUIDGeneratorUtil::getInstance).thenReturn(uuidGeneratorUtil);

            when(jwtTokenUtil.generateToken(anyMap(), eq(mockUUID))).thenReturn("mockedToken");

            // Ação
            LoginResponseDTO responseDTO = authEmployeeService.auth(loginRequestDTO);

            // Verificação
            assertNotNull(responseDTO);
            assertEquals("mockedToken", responseDTO.getToken());
            verify(employeeService, times(1)).authTemporaryPassword(loginRequestDTO);
            verify(employeeService, times(1)).authenticatedTemporaryPassword(loginRequestDTO, true);
            verify(employeeService, never()).authenticatedOldPassword(any());
        }
    }

    @Test
    void testAuth_withTemporaryPasswordAndOldPasswordFailure_shouldThrowException() throws Exception {
        // Mock do primeiro authenticate falhando
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("INVALID_CREDENTIALS"));

        // Mock do employeeService.authTemporaryPassword falhando
        doThrow(new BadCredentialsException("TEMPORARY_PASSWORD_FAILED", null)).when(employeeService).authTemporaryPassword(any());

        // Ação e Verificação
        assertThrows(BadCredentialsException.class, () -> authEmployeeService.auth(loginRequestDTO));
    }


    // --- Testes para o método loadUserByUsername() ---

    @Test
    void testLoadUserByUsername_withExistingUser_shouldReturnUserDetails() {
        // Cenário
        when(employeeRepository.findByCpf(anyString())).thenReturn(Optional.of(employee));

        // Ação
        UserDetails userDetails = authEmployeeService.loadUserByUsername(employee.getCpf());

        // Verificação
        assertNotNull(userDetails);
        assertEquals(employee.getCpf(), userDetails.getUsername());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTOR")));
        assertEquals(employee.getPassword(), userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_withTemporaryPasswordFlag_shouldUseTemporaryPassword() {
        // Cenário
        employee.setUseTemporaryPassword(true);
        employee.setTemporaryPassword("tempPassword123");
        String usernameWithFlag = employee.getCpf() + ":USETEMPORARYPASSWORD";

        when(employeeRepository.findByCpf(anyString())).thenReturn(Optional.of(employee));

        // Ação
        UserDetails userDetails = authEmployeeService.loadUserByUsername(usernameWithFlag);

        // Verificação
        assertNotNull(userDetails);
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("USETEMPORARYPASSWORD")));
        assertNotNull(userDetails.getPassword());
    }

    @Test
    void testLoadUserByUsername_withMD5Flag_shouldEncodePassword() {

        Employee employeeMD5 = new Employee();
        employeeMD5.setCpf("12345678900");
        employeeMD5.setPassword("senha123"); // A senha não está codificada
        employeeMD5.setUseTemporaryPassword(false);

        String usernameWithFlag = employeeMD5.getCpf() + ":MD5";

        when(employeeRepository.findByCpf(anyString())).thenReturn(Optional.of(employeeMD5));

        UserDetails userDetails = authEmployeeService.loadUserByUsername(usernameWithFlag);
        assertNotNull(userDetails);
        assertTrue(new BCryptPasswordEncoder().matches(employeeMD5.getPassword(), userDetails.getPassword()));
    }

    @Test
    void testLoadUserByUsername_withNonExistentUser_shouldThrowException() {
        // Cenário
        when(employeeRepository.findByCpf(anyString())).thenReturn(Optional.empty());

        // Ação e Verificação
        assertThrows(UsernameNotFoundException.class, () -> authEmployeeService.loadUserByUsername("nonexistent"));
    }
}