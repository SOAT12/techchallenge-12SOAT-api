package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.CustomerMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.EmployeeMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.ServiceOrderMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper.VehicleMapper;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.repository.jpa.ServiceOrderJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServiceOrderRepositoryImplTest {

    @Mock
    private ServiceOrderJpaRepository serviceOrderJpaRepository;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ServiceOrderMapper serviceOrderMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private VehicleMapper vehicleMapper;

    @InjectMocks
    private ServiceOrderRepositoryImpl serviceOrderRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class FindAll {
        @Test
        void shouldFindAll() {
            // Arrange
            when(serviceOrderJpaRepository.findAll()).thenReturn(List.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            List<ServiceOrder> result = serviceOrderRepository.findAll();

            // Assert
            assertFalse(result.isEmpty());
            verify(serviceOrderJpaRepository).findAll();
        }
    }

    @Nested
    class FindById {
        @Test
        void shouldFindById() {
            // Arrange
            Long id = 1L;
            when(serviceOrderJpaRepository.findById(id)).thenReturn(Optional.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            Optional<ServiceOrder> result = serviceOrderRepository.findById(id);

            // Assert
            assertNotNull(result);
            verify(serviceOrderJpaRepository).findById(id);
        }
    }

    @Nested
    class Save {
        @Test
        void shouldSave() {
            // Arrange
            ServiceOrder serviceOrder = new ServiceOrder();
            serviceOrder.setCustomer(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer.builder().id(1L).build());
            serviceOrder.setVehicle(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle.builder().id(1L).build());
            serviceOrder.setEmployee(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee.builder().id(1L).build());
            serviceOrder.setServices(new java.util.HashSet<>());
            serviceOrder.setStockItems(new java.util.HashSet<>());

            when(entityManager.getReference(any(), anyLong())).thenAnswer(i -> {
                Class<?> clazz = i.getArgument(0);
                if (clazz.equals(com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity.class)) {
                    return new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity();
                } else if (clazz.equals(com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity.class)) {
                    return new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity();
                } else if (clazz.equals(com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity.class)) {
                    return new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity();
                }
                return new Object();
            });
            when(serviceOrderMapper.toServiceOrderEntity(any(), any(), any(), any(), any(), any())).thenReturn(new ServiceOrderEntity());
            when(serviceOrderJpaRepository.save(any())).thenReturn(new ServiceOrderEntity());
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            ServiceOrder result = serviceOrderRepository.save(serviceOrder);

            // Assert
            assertNotNull(result);
            verify(serviceOrderJpaRepository).save(any());
        }
    }

    @Nested
    class FindAllFilteredAndSorted {
        @Test
        void shouldFindAllFilteredAndSorted() {
            // Arrange
            when(serviceOrderJpaRepository.findAllFilteredAndSorted(any())).thenReturn(List.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            List<ServiceOrder> result = serviceOrderRepository.findAllFilteredAndSorted(List.of());

            // Assert
            assertFalse(result.isEmpty());
            verify(serviceOrderJpaRepository).findAllFilteredAndSorted(any());
        }
    }

    @Nested
    class FindAllWithFilters {
        @Test
        void shouldFindAllWithFilters() {
            // Arrange
            when(serviceOrderJpaRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(List.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            List<ServiceOrder> result = serviceOrderRepository.findAllWithFilters(null, null, null);

            // Assert
            assertFalse(result.isEmpty());
            verify(serviceOrderJpaRepository).findAll(any(org.springframework.data.jpa.domain.Specification.class));
        }
    }

    @Nested
    class CountByEmployeeAndStatusIn {
        @Test
        void shouldCountByEmployeeAndStatusIn() {
            // Arrange
            when(employeeMapper.toEmployeeJpaEntity(any())).thenReturn(new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity());
            when(serviceOrderJpaRepository.countByEmployeeAndStatusIn(any(), any())).thenReturn(1L);

            // Act
            Long result = serviceOrderRepository.countByEmployeeAndStatusIn(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee.builder().build(), List.of());

            // Assert
            assertNotNull(result);
            verify(serviceOrderJpaRepository).countByEmployeeAndStatusIn(any(), any());
        }
    }

    @Nested
    class FindByEmployeeAndStatusIn {
        @Test
        void shouldFindByEmployeeAndStatusIn() {
            // Arrange
            when(employeeMapper.toEmployeeJpaEntity(any())).thenReturn(new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity());
            when(serviceOrderJpaRepository.findByEmployeeAndStatusIn(any(), any())).thenReturn(List.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            List<ServiceOrder> result = serviceOrderRepository.findByEmployeeAndStatusIn(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee.builder().build(), List.of());

            // Assert
            assertFalse(result.isEmpty());
            verify(serviceOrderJpaRepository).findByEmployeeAndStatusIn(any(), any());
        }
    }

    @Nested
    class FindByCustomerAndFinishedAtIsNull {
        @Test
        void shouldFindByCustomerAndFinishedAtIsNull() {
            // Arrange
            when(customerMapper.toCustomerJpaEntity(any())).thenReturn(new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity());
            when(serviceOrderJpaRepository.findByCustomerAndFinishedAtIsNull(any())).thenReturn(List.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            List<ServiceOrder> result = serviceOrderRepository.findByCustomerAndFinishedAtIsNull(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer.builder().build());

            // Assert
            assertFalse(result.isEmpty());
            verify(serviceOrderJpaRepository).findByCustomerAndFinishedAtIsNull(any());
        }
    }

    @Nested
    class FindByVehicleAndFinishedAtIsNull {
        @Test
        void shouldFindByVehicleAndFinishedAtIsNull() {
            // Arrange
            when(vehicleMapper.toVehicleJpaEntity(any())).thenReturn(new com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity());
            when(serviceOrderJpaRepository.findByVehicleAndFinishedAtIsNull(any())).thenReturn(List.of(new ServiceOrderEntity()));
            when(serviceOrderMapper.toServiceOrder(any())).thenReturn(new ServiceOrder());

            // Act
            List<ServiceOrder> result = serviceOrderRepository.findByVehicleAndFinishedAtIsNull(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle.builder().build());

            // Assert
            assertFalse(result.isEmpty());
            verify(serviceOrderJpaRepository).findByVehicleAndFinishedAtIsNull(any());
        }
    }
}