package com.fiap.soat12.tc_group_7.specification;

import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ServiceOrderSpecificationTest {

    @Test
    public void testWithFilters_noFilters() {
        Specification<ServiceOrder> spec = ServiceOrderSpecification.withFilters(null, null, null);

        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        Root<ServiceOrder> root = Mockito.mock(Root.class);

        Path<Object> finishedAtPath = Mockito.mock(Path.class);

        Mockito.when(root.get("finishedAt")).thenReturn(finishedAtPath);

        Predicate isNotNullPredicate = Mockito.mock(Predicate.class);
        Predicate conjunctionPredicate = Mockito.mock(Predicate.class);

        Mockito.when(cb.isNotNull(finishedAtPath)).thenReturn(isNotNullPredicate);

        Mockito.when(cb.and(Mockito.any(Predicate[].class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return args.length > 0 ? (Predicate) args[0] : conjunctionPredicate;
        });

        Predicate predicate = spec.toPredicate(root, query, cb);

        assertThat(predicate).isNotNull();
    }

    @Test
    public void testWithFilters_withAllFilters() {
        Date startDate = new Date(System.currentTimeMillis() - 3600_000);
        Date endDate = new Date();
        List<Long> serviceIds = Arrays.asList(1L, 2L, 3L);

        Specification<ServiceOrder> spec = ServiceOrderSpecification.withFilters(startDate, endDate, serviceIds);

        CriteriaBuilder cb = Mockito.mock(CriteriaBuilder.class);
        CriteriaQuery<?> query = Mockito.mock(CriteriaQuery.class);
        Root<ServiceOrder> root = Mockito.mock(Root.class);

        Path<Date> finishedAtPath = Mockito.mock(Path.class);
        Path<Date> createdAtPath = Mockito.mock(Path.class);
        Join<Object, Object> servicesJoin = Mockito.mock(Join.class);
        Path<Object> vehicleServicePath = Mockito.mock(Path.class);
        Path<Object> idPath = Mockito.mock(Path.class);

        Mockito.when(root.<Date>get("finishedAt")).thenReturn(finishedAtPath);
        Mockito.when(root.<Date>get("createdAt")).thenReturn(createdAtPath);
        Mockito.when(root.join("services")).thenReturn(servicesJoin);
        Mockito.when(servicesJoin.get("vehicleService")).thenReturn(vehicleServicePath);
        Mockito.when(vehicleServicePath.get("id")).thenReturn(idPath);

        Predicate isNotNullPredicate = Mockito.mock(Predicate.class);
        Predicate greaterThanEqualPredicate = Mockito.mock(Predicate.class);
        Predicate lessThanEqualPredicate = Mockito.mock(Predicate.class);
        Predicate inPredicate = Mockito.mock(Predicate.class);
        Predicate conjunctionPredicate = Mockito.mock(Predicate.class);

        Mockito.when(cb.isNotNull(finishedAtPath)).thenReturn(isNotNullPredicate);
        Mockito.when(cb.greaterThanOrEqualTo(createdAtPath, startDate)).thenReturn(greaterThanEqualPredicate);
        Mockito.when(cb.lessThanOrEqualTo(finishedAtPath, endDate)).thenReturn(lessThanEqualPredicate);
        Mockito.when(idPath.in(serviceIds)).thenReturn(inPredicate);

        Mockito.when(cb.and(Mockito.any(Predicate[].class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return args.length > 0 ? (Predicate) args[0] : conjunctionPredicate;
        });

        Predicate predicate = spec.toPredicate(root, query, cb);

        assertThat(predicate).isNotNull();
    }

}
