package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.ChangePasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.ForgotPasswordRequestDTO;
import com.fiap.soat12.tc_group_7.dto.LoginRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeRequestDTO;
import com.fiap.soat12.tc_group_7.dto.employee.EmployeeResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Employee;
import com.fiap.soat12.tc_group_7.entity.EmployeeFunction;
import com.fiap.soat12.tc_group_7.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.repository.EmployeeFunctionRepository;
import com.fiap.soat12.tc_group_7.repository.EmployeeRepository;
import com.fiap.soat12.tc_group_7.util.CodeGenerator;
import com.fiap.soat12.tc_group_7.util.CryptUtil;
import com.fiap.soat12.tc_group_7.util.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeFunctionRepository employeeFunctionRepository;
    @Mock
    private MailClient mailClient;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;
    private EmployeeFunction employeeFunction;
    private EmployeeRequestDTO requestDTO;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        employeeFunction = EmployeeFunction.builder()
                .id(1L)
                .description("Gerente")
                .build();
        employee = Employee.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .employeeFunction(employeeFunction)
                .password("$2a$10$fA1h1y.5xGfD9Gj1Jz71iO.S1gQe.H5E.2O9J5gQ.P6g") // "senha123"
                .build();
        requestDTO = EmployeeRequestDTO.builder()
                .employeeFunctionId(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .password("senha123")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .build();
        responseDTO = EmployeeResponseDTO.builder()
                .id(1L)
                .cpf("123.456.789-00")
                .name("Maria Souza")
                .phone("88888-8888")
                .email("maria@email.com")
                .active(true)
                .created_at(new Date())
                .updated_at(new Date())
                .employeeFunction(null)
                .build();
    }

    @Test
    @DisplayName("Deve listar todos os funcionários")
    void shouldListAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        List<EmployeeResponseDTO> result = employeeService.getAllEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Maria Souza", result.get(0).getName());
    }

    @Test
    @DisplayName("Deve listar todos os funcionários ativos")
    void shouldListAllActiveEmployees() {
        when(employeeRepository.findAllByActiveTrue()).thenReturn(List.of(employee));
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        List<EmployeeResponseDTO> result = employeeService.getAllActiveEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getActive());
    }

    @Test
    @DisplayName("Deve retornar funcionário por ID")
    void shouldReturnEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        Optional<EmployeeResponseDTO> result = employeeService.getEmployeeById(1L);
        assertTrue(result.isPresent());
        assertEquals("Maria Souza", result.get().getName());
    }

    @Test
    @DisplayName("Deve criar funcionário com sucesso")
    void shouldCreateEmployeeSuccessfully() {
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        when(employeeMapper.toEmployee(requestDTO, employeeFunction)).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        EmployeeResponseDTO result = employeeService.createEmployee(requestDTO);
        assertNotNull(result);
        assertEquals("Maria Souza", result.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar funcionário com função inexistente")
    void shouldThrowExceptionWhenCreatingEmployeeWithNonExistentFunction() {
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        requestDTO.setEmployeeFunctionId(99L);
        assertThrows(IllegalArgumentException.class, () -> employeeService.createEmployee(requestDTO));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve atualizar funcionário existente com sucesso")
    void shouldUpdateEmployeeSuccessfully() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeFunctionRepository.findById(1L)).thenReturn(Optional.of(employeeFunction));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(responseDTO);
        Optional<EmployeeResponseDTO> result = employeeService.updateEmployee(1L, requestDTO);
        assertTrue(result.isPresent());
        assertEquals("Maria Souza", result.get().getName());
    }

    @Test
    @DisplayName("Não deve atualizar funcionário se não for encontrado")
    void shouldNotUpdateEmployeeIfNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<EmployeeResponseDTO> result = employeeService.updateEmployee(99L, requestDTO);
        assertFalse(result.isPresent());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar funcionário com função inexistente")
    void shouldThrowExceptionWhenUpdatingEmployeeWithNonExistentFunction() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeFunctionRepository.findById(99L)).thenReturn(Optional.empty());
        requestDTO.setEmployeeFunctionId(99L);
        assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(1L, requestDTO));
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve inativar funcionário com sucesso")
    void shouldInactivateEmployeeSuccessfully() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        boolean result = employeeService.inactivateEmployee(1L);
        assertTrue(result);
        assertFalse(employee.getActive());
    }

    @Test
    @DisplayName("Não deve inativar funcionário se não for encontrado")
    void shouldNotInactivateEmployeeIfNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = employeeService.inactivateEmployee(99L);
        assertFalse(result);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve ativar funcionário com sucesso")
    void shouldActivateEmployeeSuccessfully() {
        employee.setActive(false);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        boolean result = employeeService.activateEmployee(1L);
        assertTrue(result);
        assertTrue(employee.getActive());
    }

    @Test
    @DisplayName("Não deve ativar funcionário se não for encontrado")
    void shouldNotActivateEmployeeIfNotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        boolean result = employeeService.activateEmployee(99L);
        assertFalse(result);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    @DisplayName("Deve lançar exceção se a nova senha e a confirmação não corresponderem")
    void changePassword_withMismatchingPasswords_shouldThrowException() {
        ChangePasswordRequestDTO requestDTO = new ChangePasswordRequestDTO("senha123", "novasenha123", "senhaerrada");
        assertThrows(BadCredentialsException.class, () -> employeeService.changePassword(1L, requestDTO));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve enviar senha temporária por email para redefinição de senha")
    void forgotPassword_withValidCpf_shouldSendEmailAndSaveTempPassword() throws Exception {
        try (MockedStatic<CodeGenerator> mockedCodeGenerator = Mockito.mockStatic(CodeGenerator.class);
             MockedStatic<CryptUtil> mockedCryptUtil = Mockito.mockStatic(CryptUtil.class);
             MockedStatic<DateUtils> mockedDateUtils = Mockito.mockStatic(DateUtils.class)) {

            ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("123.456.789-00");
            when(employeeRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(employee));
            mockedCodeGenerator.when(CodeGenerator::generateCode).thenReturn("TEMPPWD");
            mockedCryptUtil.when(() -> CryptUtil.bcrypt("temppwd")).thenReturn("temp_password_hash");
            mockedDateUtils.when(() -> DateUtils.toLocalDateTime(any(Date.class))).thenReturn(LocalDateTime.now());
            doNothing().when(mailClient).sendMail(any(), any(), any(), any());

            employeeService.forgotPassword(requestDTO);

            verify(mailClient, times(1)).sendMail(eq("maria@email.com"), eq("Redefinição de Senha"), eq("mailTemplate"), any());
            verify(employeeRepository, times(1)).save(employee);
            assertEquals("temp_password_hash", employee.getTemporaryPassword());
            assertTrue(employee.getUseTemporaryPassword());
        }
    }

    @Test
    @DisplayName("Deve lançar exceção se o CPF não for encontrado na redefinição de senha")
    void forgotPassword_withInvalidCpf_shouldThrowException() throws Exception {
        ForgotPasswordRequestDTO requestDTO = new ForgotPasswordRequestDTO("999.999.999-99");
        when(employeeRepository.findByCpf("999.999.999-99")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> employeeService.forgotPassword(requestDTO));
        verify(mailClient, never()).sendMail(any(), any(), any(), any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar a senha temporária se ela for válida")
    void authTemporaryPassword_withValidTempPassword_shouldSucceed() throws Exception {
        try (MockedStatic<DateUtils> mockedDateUtils = Mockito.mockStatic(DateUtils.class)) {
            employee.setTemporaryPassword("temp_hash");
            employee.setPasswordValidity(LocalDateTime.now().minusMinutes(10));
            employee.setUseTemporaryPassword(true);

            when(employeeRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(employee));
            mockedDateUtils.when(() -> DateUtils.toDate(any(LocalDateTime.class))).thenReturn(new Date());
            mockedDateUtils.when(() -> DateUtils.minutesDiff(any(Date.class), any(Date.class))).thenReturn(10);
            mockedDateUtils.when(DateUtils::getCurrentDate).thenReturn(new Date());

            employeeService.authTemporaryPassword(new LoginRequestDTO("123.456.789-00", "temp_password"));

            verify(employeeRepository, times(1)).findByCpf("123.456.789-00");
        }
    }

    @Test
    @DisplayName("Deve falhar se a senha temporária estiver expirada ou for inválida")
    void authTemporaryPassword_withExpiredTempPassword_shouldThrowException() {
        try (MockedStatic<DateUtils> mockedDateUtils = Mockito.mockStatic(DateUtils.class)) {
            employee.setTemporaryPassword("temp_hash");
            employee.setPasswordValidity(LocalDateTime.now().minusMinutes(601));
            employee.setUseTemporaryPassword(true);

            when(employeeRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(employee));
            mockedDateUtils.when(() -> DateUtils.toDate(any(LocalDateTime.class))).thenReturn(new Date());
            mockedDateUtils.when(() -> DateUtils.minutesDiff(any(Date.class), any(Date.class))).thenReturn(601);
            mockedDateUtils.when(DateUtils::getCurrentDate).thenReturn(new Date());

            assertThrows(Exception.class, () -> employeeService.authTemporaryPassword(new LoginRequestDTO("123.456.789-00", "temp_password")));

            verify(employeeRepository, times(1)).save(employee);
            assertEquals("", employee.getTemporaryPassword());
            assertFalse(employee.getUseTemporaryPassword());
        }
    }

    @Test
    @DisplayName("Deve atualizar a senha principal e desativar a temporária se 'usedTmp' for verdadeiro")
    void authenticatedTemporaryPassword_withUsedTmpTrue_shouldUpdatePassword() throws Exception {
        try (MockedStatic<CryptUtil> mockedCryptUtil = Mockito.mockStatic(CryptUtil.class)) {
            LoginRequestDTO requestDTO = new LoginRequestDTO("123.456.789-00", "novasenha");
            employee.setUseTemporaryPassword(true);
            employee.setPassword("senhaantiga_hash");

            when(employeeRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(employee));
            mockedCryptUtil.when(() -> CryptUtil.bcrypt("novasenha")).thenReturn("novasenha_hash");

            employeeService.authenticatedTemporaryPassword(requestDTO, true);

            verify(employeeRepository, times(1)).save(employee);
            assertEquals("novasenha_hash", employee.getPassword());
            assertEquals("", employee.getTemporaryPassword());
            assertFalse(employee.getUseTemporaryPassword());
        }
    }

    @Test
    @DisplayName("Deve apenas desativar a senha temporária se 'usedTmp' for falso")
    void authenticatedTemporaryPassword_withUsedTmpFalse_shouldNotUpdatePassword() throws Exception {
        LoginRequestDTO requestDTO = new LoginRequestDTO("123.456.789-00", "novasenha");
        String originalPasswordHash = employee.getPassword();
        employee.setUseTemporaryPassword(true);
        employee.setTemporaryPassword("temp_hash");

        when(employeeRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(employee));

        employeeService.authenticatedTemporaryPassword(requestDTO, false);

        verify(employeeRepository, times(1)).save(employee);
        assertEquals(originalPasswordHash, employee.getPassword());
        assertEquals("", employee.getTemporaryPassword());
        assertFalse(employee.getUseTemporaryPassword());
    }

    @Test
    @DisplayName("Deve redefinir a senha temporária para login com a senha antiga")
    void authenticatedOldPassword_shouldResetTempPassword() throws Exception {
        try (MockedStatic<CryptUtil> mockedCryptUtil = Mockito.mockStatic(CryptUtil.class)) {
            LoginRequestDTO requestDTO = new LoginRequestDTO("123.456.789-00", "senha123");
            employee.setUseTemporaryPassword(true);
            employee.setTemporaryPassword("temp_hash");

            when(employeeRepository.findByCpf("123.456.789-00")).thenReturn(Optional.of(employee));
            mockedCryptUtil.when(() -> CryptUtil.bcrypt("senha123")).thenReturn("senha123_hash_updated");

            employeeService.authenticatedOldPassword(requestDTO);

            verify(employeeRepository, times(1)).save(employee);
            assertEquals("senha123_hash_updated", employee.getPassword());
            assertEquals("", employee.getTemporaryPassword());
            assertFalse(employee.getUseTemporaryPassword());
        }
    }
}