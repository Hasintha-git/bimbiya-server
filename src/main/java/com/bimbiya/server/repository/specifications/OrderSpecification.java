package com.bimbiya.server.repository.specifications;

import com.bimbiya.server.dto.filter.OrderFilterDTO;
import com.bimbiya.server.entity.*;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class OrderSpecification {

    public Specification<Order> generateSearchCriteria(OrderFilterDTO orderFilterDTO) {
        return (Specification<Order>)(root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(orderFilterDTO.getUserId()) )
                predicates.add(criteriaBuilder.equal(root.get(Order_.SYSTEM_USER).get(SystemUser_.ID), orderFilterDTO.getUserId()));

            if (Objects.nonNull(orderFilterDTO.getStatus()) && !orderFilterDTO.getStatus().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(Order_.STATUS), Status.valueOf(orderFilterDTO.getStatus())));

            if (Objects.nonNull(orderFilterDTO.getOrderDate()) )
                predicates.add(criteriaBuilder.equal(root.get(Order_.CREATED_TIME), new Date(String.valueOf(orderFilterDTO.getOrderDate()))));


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
