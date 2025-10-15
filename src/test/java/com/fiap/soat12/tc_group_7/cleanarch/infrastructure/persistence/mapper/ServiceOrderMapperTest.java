package com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.mapper;

import com.fiap.soat12.tc_group_7.cleanarch.domain.model.ServiceOrder;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.CustomerJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.ServiceOrderEntity;
import com.fiap.soat12.tc_group_7.cleanarch.infrastructure.persistence.entity.VehicleJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ServiceOrderMapperTest {

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private VehicleServiceMapper vehicleServiceMapper;

    @Mock
    private StockMapper stockMapper;

    @InjectMocks
    private ServiceOrderMapper serviceOrderMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class ToServiceOrder {
        @Test
        void shouldConvertToServiceOrder() {
            // Arrange
            ServiceOrderEntity entity = new ServiceOrderEntity();
            entity.setCustomer(new CustomerJpaEntity());
            entity.setVehicle(new VehicleJpaEntity());
            entity.setEmployee(new EmployeeJpaEntity());
            entity.setServices(new HashSet<>());
            entity.setStockItems(new HashSet<>());

            when(customerMapper.toCustomer(any())).thenReturn(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Customer.builder().build());
            when(vehicleMapper.toVehicle(any())).thenReturn(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Vehicle.builder().build());
            when(employeeMapper.toEmployee(any())).thenReturn(com.fiap.soat12.tc_group_7.cleanarch.domain.model.Employee.builder().build());

            // Act
            ServiceOrder result = serviceOrderMapper.toServiceOrder(entity);

            // Assert
            assertNotNull(result);
        }
    }

    @Nested
    class ToServiceOrderEntity {
        @Test
        void shouldConvertToServiceOrderEntity() {
            // Arrange
            ServiceOrder serviceOrder = new ServiceOrder();
            serviceOrder.setId(1L);

            // Act
            ServiceOrderEntity result = serviceOrderMapper.toServiceOrderEntity(serviceOrder, new CustomerJpaEntity(), new VehicleJpaEntity(), new EmployeeJpaEntity(), new HashSet<>(), new HashSet<>());

            // Assert
            assertNotNull(result);
        }
    }
}
