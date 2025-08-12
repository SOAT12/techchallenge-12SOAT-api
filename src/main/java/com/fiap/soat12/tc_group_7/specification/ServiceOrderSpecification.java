package com.fiap.soat12.tc_group_7.specification;

import com.fiap.soat12.tc_group_7.entity.ServiceOrder;
import com.fiap.soat12.tc_group_7.entity.ServiceOrderVehicleService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceOrderSpecification {

    public static Specification<ServiceOrder> withFilters(Date startDate, Date endDate, List<Long> serviceIds) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.isNotNull(root.get("finishedAt")));

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
            }

            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("finishedAt"), endDate));
            }

            if (serviceIds != null && !serviceIds.isEmpty()) {
                Join<ServiceOrder, ServiceOrderVehicleService> servicesJoin = root.join("services");
                predicates.add(servicesJoin.get("vehicleService").get("id").in(serviceIds));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
