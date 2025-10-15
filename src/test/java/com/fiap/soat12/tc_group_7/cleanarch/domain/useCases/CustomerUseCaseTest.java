package com.fiap.soat12.tc_group_7.cleanarch.domain.useCases;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer;
import com.fiap.soat12.tc_group_7.cleanarch.exception.NotFoundException;
import com.fiap.soat12.tc_group_7.cleanarch.gateway.CustomerGateway;
import com.fiap.soat12.tc_group_7.dto.customer.CustomerRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerUseCaseTest {

    @Mock
    private CustomerGateway customerGateway;

    @InjectMocks
    private CustomerUseCase customerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class GetAllActiveCustomers {

        @Test
        void shouldReturnOnlyActiveCustomers() {
            // Arrange
            Customer activeCustomer = Customer.builder().id(1L).deleted(false).build();
            Customer deletedCustomer = Customer.builder().id(2L).deleted(true).build();
            when(customerGateway.findAll()).thenReturn(List.of(activeCustomer, deletedCustomer));

            // Act
            List<Customer> result = customerUseCase.getAllActiveCustomers();

            // Assert
            assertEquals(1, result.size());
            assertEquals(activeCustomer, result.get(0));
            verify(customerGateway, times(1)).findAll();
        }
    }

    @Nested
    class GetAllCustomers {

        @Test
        void shouldReturnAllCustomers() {
            // Arrange
            Customer customer1 = Customer.builder().id(1L).build();
            Customer customer2 = Customer.builder().id(2L).build();
            when(customerGateway.findAll()).thenReturn(List.of(customer1, customer2));

            // Act
            List<Customer> result = customerUseCase.getAllCustomers();

            // Assert
            assertEquals(2, result.size());
            verify(customerGateway, times(1)).findAll();
        }
    }

    @Nested
    class GetCustomerById {

        @Test
        void shouldReturnCustomerWhenFound() {
            // Arrange
            Long customerId = 1L;
            Customer customer = Customer.builder().id(customerId).build();
            when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));

            // Act
            Customer result = customerUseCase.getCustomerById(customerId);

            // Assert
            assertNotNull(result);
            assertEquals(customerId, result.getId());
            verify(customerGateway, times(1)).findById(customerId);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenCustomerNotFound() {
            // Arrange
            Long customerId = 1L;
            when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> customerUseCase.getCustomerById(customerId));
            verify(customerGateway, times(1)).findById(customerId);
        }
    }

    @Nested
    class GetCustomerByCpf {

        @Test
        void shouldReturnCustomerWhenFound() {
            // Arrange
            String cpf = "12345678900";
            Customer customer = Customer.builder().cpf(cpf).build();
            when(customerGateway.findByCpf(cpf)).thenReturn(Optional.of(customer));

            // Act
            Customer result = customerUseCase.getCustomerByCpf(cpf);

            // Assert
            assertNotNull(result);
            assertEquals(cpf, result.getCpf());
            verify(customerGateway, times(1)).findByCpf(cpf);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenCustomerNotFound() {
            // Arrange
            String cpf = "12345678900";
            when(customerGateway.findByCpf(cpf)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> customerUseCase.getCustomerByCpf(cpf));
            verify(customerGateway, times(1)).findByCpf(cpf);
        }
    }

    @Nested
    class CreateCustomer {

        @Test
        void shouldCreateCustomerSuccessfully() {
            // Arrange
            CustomerRequestDTO requestDTO = new CustomerRequestDTO();
            requestDTO.setCpf("12345678900");
            when(customerGateway.findByCpf(requestDTO.getCpf())).thenReturn(Optional.empty());

            Customer customerToSave = Customer.builder()
                    .cpf(requestDTO.getCpf())
                    .deleted(false)
                    .build();
            when(customerGateway.save(any(Customer.class))).thenReturn(customerToSave);


            // Act
            Customer result = customerUseCase.createCustomer(requestDTO);

            // Assert
            assertNotNull(result);
            assertEquals(requestDTO.getCpf(), result.getCpf());
            assertFalse(result.getDeleted());
            verify(customerGateway, times(1)).findByCpf(requestDTO.getCpf());
            verify(customerGateway, times(1)).save(any(Customer.class));
        }

        @Test
        void shouldThrowIllegalArgumentExceptionWhenCustomerAlreadyExists() {
            // Arrange
            CustomerRequestDTO requestDTO = new CustomerRequestDTO();
            requestDTO.setCpf("12345678900");
            when(customerGateway.findByCpf(requestDTO.getCpf())).thenReturn(Optional.of(Customer.builder().build()));

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> customerUseCase.createCustomer(requestDTO));
            verify(customerGateway, times(1)).findByCpf(requestDTO.getCpf());
            verify(customerGateway, never()).save(any(Customer.class));
        }
    }

    @Nested
    class UpdateCustomerById {

        @Test
        void shouldUpdateCustomerSuccessfully() {
            // Arrange
            Long customerId = 1L;
            CustomerRequestDTO requestDTO = new CustomerRequestDTO();
            requestDTO.setName("New Name");
            Customer existingCustomer = Customer.builder().id(customerId).name("Old Name").build();
            when(customerGateway.findById(customerId)).thenReturn(Optional.of(existingCustomer));

            // Act
            Customer result = customerUseCase.updateCustomerById(customerId, requestDTO);

            // Assert
            assertNotNull(result);
            assertEquals("New Name", result.getName());
            verify(customerGateway, times(1)).findById(customerId);
            verify(customerGateway, times(1)).update(existingCustomer);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenCustomerToUpdateNotFound() {
            // Arrange
            Long customerId = 1L;
            CustomerRequestDTO requestDTO = new CustomerRequestDTO();
            when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> customerUseCase.updateCustomerById(customerId, requestDTO));
            verify(customerGateway, times(1)).findById(customerId);
            verify(customerGateway, never()).update(any(Customer.class));
        }
    }

    @Nested
    class DeleteCustomerById {

        @Test
        void shouldSetDeletedToTrue() {
            // Arrange
            Long customerId = 1L;
            Customer customer = Customer.builder().id(customerId).deleted(false).build();
            when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));

            // Act
            customerUseCase.deleteCustomerById(customerId);

            // Assert
            assertTrue(customer.getDeleted());
            verify(customerGateway, times(1)).findById(customerId);
            verify(customerGateway, times(1)).update(customer);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenCustomerToDeleteNotFound() {
            // Arrange
            Long customerId = 1L;
            when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> customerUseCase.deleteCustomerById(customerId));
            verify(customerGateway, times(1)).findById(customerId);
            verify(customerGateway, never()).update(any(Customer.class));
        }
    }

    @Nested
    class ActivateCustomer {

        @Test
        void shouldSetDeletedToFalse() {
            // Arrange
            Long customerId = 1L;
            Customer customer = Customer.builder().id(customerId).deleted(true).build();
            when(customerGateway.findById(customerId)).thenReturn(Optional.of(customer));

            // Act
            customerUseCase.activateCustomer(customerId);

            // Assert
            assertFalse(customer.getDeleted());
            verify(customerGateway, times(1)).findById(customerId);
            verify(customerGateway, times(1)).update(customer);
        }

        @Test
        void shouldThrowNotFoundExceptionWhenCustomerToActivateNotFound() {
            // Arrange
            Long customerId = 1L;
            when(customerGateway.findById(customerId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(NotFoundException.class, () -> customerUseCase.activateCustomer(customerId));
            verify(customerGateway, times(1)).findById(customerId);
            verify(customerGateway, never()).update(any(Customer.class));
        }
    }
}