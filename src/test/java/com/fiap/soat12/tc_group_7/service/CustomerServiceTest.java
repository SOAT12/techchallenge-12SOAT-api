package com.fiap.soat12.tc_group_7.service;

import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerResponseDTO;
import com.fiap.soat12.tc_group_7.entity.Customer;
import com.fiap.soat12.tc_group_7.exception.BusinessException;
import com.fiap.soat12.tc_group_7.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllActiveCustomers_withSuccess() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .build();
        CustomerResponseDTO dto = CustomerResponseDTO.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .build();

        when(customerRepository.findAllByDeletedFalse()).thenReturn(List.of(customer));
        when(customerMapper.toCustomerResponseDTO(customer)).thenReturn(dto);

        // Act
        List<CustomerResponseDTO> result = customerService.getAllActiveCustomers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getName());
        assertEquals("123.456.789-00", result.get(0).getCpf());
        verify(customerRepository, times(1)).findAllByDeletedFalse();
        verify(customerMapper, times(1)).toCustomerResponseDTO(customer);
    }

    @Test
    void getAllCustomers_withSuccess() {
        // Arrange
        Customer customer = Customer.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .build();
        CustomerResponseDTO dto = CustomerResponseDTO.builder()
                .id(1L)
                .name("João Silva")
                .cpf("123.456.789-00")
                .build();

        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.toCustomerResponseDTO(customer)).thenReturn(dto);

        // Act
        List<CustomerResponseDTO> result = customerService.getAllCustomers();

        // Assert
        assertEquals(1, result.size());
        assertEquals("João Silva", result.get(0).getName());
        assertEquals("123.456.789-00", result.get(0).getCpf());
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toCustomerResponseDTO(customer);
    }

    @Test
    void getCustomerByCpf_withSuccess() {
        // Arrange
        String cpf = "123.456.789-00";
        Customer customer = Customer.builder()
                .id(1L)
                .cpf(cpf)
                .name("João")
                .build();

        CustomerResponseDTO dto = CustomerResponseDTO.builder()
                .id(1L)
                .cpf(cpf)
                .name("João")
                .build();

        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomerResponseDTO(customer)).thenReturn(dto);

        // Act
        CustomerResponseDTO result = customerService.getCustomerByCpf(cpf);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getCpf(), result.getCpf());
        assertEquals(dto.getName(), result.getName());
    }

    @Test
    void getCustomerByCpf_withNotFoundException() {
        // Arrange
        String cpf = "000.000.000-00";
        when(customerRepository.findByCpf(cpf)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> customerService.getCustomerByCpf(cpf));
    }

    @Test
    void createCustomer_withSuccess() {
        // Arrange
        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .cpf("12345678900")
                .name("João da Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua das Flores")
                .number("100")
                .build();
        Customer customer = Customer.builder()
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .district(requestDTO.getDistrict())
                .street(requestDTO.getStreet())
                .number(requestDTO.getNumber())
                .build();
        Customer savedCustomer = Customer.builder()
                .id(1L)
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .build();
        CustomerResponseDTO expectedResponse = CustomerResponseDTO.builder()
                .id(1L)
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .build();

        when(customerRepository.findByCpf(requestDTO.getCpf())).thenReturn(Optional.empty());
        when(customerMapper.toCustomer(requestDTO)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(savedCustomer);
        when(customerMapper.toCustomerResponseDTO(savedCustomer)).thenReturn(expectedResponse);

        // Act
        CustomerResponseDTO result = customerService.createCustomer(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("12345678900", result.getCpf());
        assertEquals("João da Silva", result.getName());
        verify(customerRepository).findByCpf(requestDTO.getCpf());
        verify(customerRepository).save(customer);
    }

    @Test
    void createCustomer_withBusinessException() {
        // Arrange
        String cpfDuplicado = "12345678900";

        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .cpf(cpfDuplicado)
                .name("João")
                .build();

        Customer existingCustomer = Customer.builder()
                .cpf(cpfDuplicado)
                .build();

        when(customerRepository.findByCpf(cpfDuplicado))
                .thenReturn(Optional.of(existingCustomer));

        // Act
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> customerService.createCustomer(requestDTO)
        );

        // Assert
        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(customerRepository).findByCpf(cpfDuplicado);
        verifyNoMoreInteractions(customerRepository);
    }

    @Test
    void updateCustomerById_withSuccess() {
        // Arrange
        Long id = 1L;
        CustomerRequestDTO requestDTO = CustomerRequestDTO.builder()
                .cpf("12345678900")
                .name("João da Silva")
                .phone("11999999999")
                .email("joao@email.com")
                .city("São Paulo")
                .state("SP")
                .district("Centro")
                .street("Rua das Flores")
                .number("100")
                .build();
        Customer existingCustomer = Customer.builder()
                .id(id)
                .build();
        Customer updatedCustomer  = Customer.builder()
                .cpf(requestDTO.getCpf())
                .name(requestDTO.getName())
                .phone(requestDTO.getPhone())
                .email(requestDTO.getEmail())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .district(requestDTO.getDistrict())
                .street(requestDTO.getStreet())
                .number(requestDTO.getNumber())
                .build();
        CustomerResponseDTO responseDTO = CustomerResponseDTO.builder()
                .id(id)
                .name("João Silva")
                .build();

        when(customerRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.toCustomerResponseDTO(updatedCustomer)).thenReturn(responseDTO);

        // Act
        CustomerResponseDTO result = customerService.updateCustomerById(id, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("João Silva", result.getName());
        verify(customerRepository).findByIdAndDeletedFalse(id);
        verify(customerRepository).save(any(Customer.class));
        verify(customerMapper).toCustomerResponseDTO(updatedCustomer);
    }

    @Test
    void updateCustomerById_withNotFoundException() {
        // Arrange
        Long id = 2L;
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();

        when(customerRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        // Act
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> customerService.updateCustomerById(id, requestDTO));

        // Assert
        assertEquals("Cliente não encontrado.", exception.getMessage());
        verify(customerRepository).findByIdAndDeletedFalse(id);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void deleteCustomerById_withSuccess() {
        // Assert
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setDeleted(false);

        when(customerRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomerById(id);

        // Assert
        assertTrue(customer.getDeleted());
        verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomerById_withNotFoundException() {
        // Assert
        Long id = 99L;
        when(customerRepository.findByIdAndDeletedFalse(id)).thenReturn(Optional.empty());

        // Act & Assert
        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            customerService.deleteCustomerById(id);
        });

        assertEquals("Cliente não encontrado.", ex.getMessage());
        verify(customerRepository, never()).save(any());
    }

    @Test
    void activateCustomer_withSuccess() {
        // Assert
        Long id = 1L;
        Customer customer = new Customer();
        customer.setId(id);
        customer.setDeleted(false);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        // Act
        customerService.activateCustomer(id);

        // Assert
        assertFalse(customer.getDeleted());
        verify(customerRepository).save(customer);
    }

}
