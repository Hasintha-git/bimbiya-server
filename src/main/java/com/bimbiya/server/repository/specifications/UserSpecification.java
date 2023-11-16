package com.bimbiya.server.repository.specifications;

import com.bimbiya.server.dto.filter.UserRequestSearchDTO;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.entity.SystemUser_;
import com.bimbiya.server.entity.UserRole_;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserSpecification {

    public Specification<SystemUser> generateSearchCriteria(UserRequestSearchDTO userRequestSearchDTO) {
        return (Specification<SystemUser>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(userRequestSearchDTO.getNic()) )
                predicates.add(criteriaBuilder.equal(root.get(SystemUser_.NIC), userRequestSearchDTO.getNic()));

            if (Objects.nonNull(userRequestSearchDTO.getUserName())  && !userRequestSearchDTO.getUserName().isEmpty())
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(SystemUser_.USERNAME)),  "%"
                        .concat(userRequestSearchDTO.getUserName().toLowerCase()).concat("%")));

            if (Objects.nonNull(userRequestSearchDTO.getMobileNo()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(SystemUser_.MOBILE_NO)),  "%"
                        .concat(userRequestSearchDTO.getMobileNo().toLowerCase()).concat("%")));

            if (Objects.nonNull(userRequestSearchDTO.getEmail()) )
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(SystemUser_.EMAIL)),  "%"
                        .concat(userRequestSearchDTO.getEmail().toLowerCase()).concat("%")));

            if (Objects.nonNull(userRequestSearchDTO.getUserRole()) )
                predicates.add(criteriaBuilder.equal(root.get(SystemUser_.USER_ROLE).get(UserRole_.CODE), userRequestSearchDTO.getUserRole()));

            if (Objects.nonNull(userRequestSearchDTO.getStatus()) && !userRequestSearchDTO.getStatus().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(SystemUser_.STATUS), Status.valueOf(userRequestSearchDTO.getStatus())));

            predicates.add(criteriaBuilder.notEqual(root.get(SystemUser_.STATUS), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<SystemUser> generateSearchCriteria(Status status) {
        return (Specification<SystemUser>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(SystemUser_.STATUS), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
