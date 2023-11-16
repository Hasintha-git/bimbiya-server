package com.bimbiya.server.repository.specifications;

import com.bimbiya.server.dto.filter.BytePackageSearchDTO;
import com.bimbiya.server.entity.BytePackage;
import com.bimbiya.server.entity.BytePackage_;
import com.bimbiya.server.util.enums.ClientStatusEnum;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class BytePackageSpecification {

    public Specification<BytePackage> generateSearchCriteria(BytePackageSearchDTO bytePackageSearchDTO) {
        return (Specification<BytePackage>)(root, query, criteriaBuilder) -> {
            System.out.println(">>>>>>>>>>>"+bytePackageSearchDTO);
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(bytePackageSearchDTO.getMealName()) )
                predicates.add(criteriaBuilder.equal(root.get(BytePackage_.MEAL_NAME), bytePackageSearchDTO.getMealName()));

            if (Objects.nonNull(bytePackageSearchDTO.getStatus()) && !bytePackageSearchDTO.getStatus().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(BytePackage_.STATUS), bytePackageSearchDTO.getStatus()));


            if (Objects.nonNull(bytePackageSearchDTO.getPortionList()))
                predicates
                        .add(root.get(BytePackage_.POTION).in(bytePackageSearchDTO.getPortionList()));

            if (Objects.nonNull(bytePackageSearchDTO.getPrice()))
                predicates.add(criteriaBuilder.equal(root.get(BytePackage_.PRICE), bytePackageSearchDTO.getPrice()));

            predicates.add(criteriaBuilder.notEqual(root.get(BytePackage_.STATUS), ClientStatusEnum.DELETED.getCode()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<BytePackage> generateSearchCriteria(Status status) {
        return (Specification<BytePackage>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(BytePackage_.STATUS), ClientStatusEnum.DELETED.getCode()));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
