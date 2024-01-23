package com.bimbiya.server.repository.specifications;

import com.bimbiya.server.dto.filter.BytePackageSearchDTO;
import com.bimbiya.server.entity.*;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class BytePackageSpecification {

    public Specification<Product> generateSearchCriteria(BytePackageSearchDTO bytePackageSearchDTO) {
        return (Specification<Product>)(root, query, criteriaBuilder) -> {
            System.out.println(">>>>>>>>>>>"+bytePackageSearchDTO);
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(bytePackageSearchDTO.getMealName()) )
                predicates.add(criteriaBuilder.equal(root.get(Product_.PRODUCT_NAME), bytePackageSearchDTO.getMealName()));

            if (Objects.nonNull(bytePackageSearchDTO.getStatus()) && !bytePackageSearchDTO.getStatus().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(Product_.STATUS), bytePackageSearchDTO.getStatus()));


            if (Objects.nonNull(bytePackageSearchDTO.getPortionList()) && bytePackageSearchDTO.getPortionList().size() != 0)
                predicates
                        .add(root.get(Product_.POTION).in(bytePackageSearchDTO.getPortionList()));

            if (Objects.nonNull(bytePackageSearchDTO.getIngredientList()) && bytePackageSearchDTO.getIngredientList().size() != 0) {
                // Join the BytePackageIngredients entity
                Join<Product, BytePackageIngredients> packageIngredientsJoin = root.join(Product_.PACKAGE_INGREDIENTS, JoinType.LEFT);

                // Join the Ingredients entity
                Join<BytePackageIngredients, Ingredients> ingredientsJoin = packageIngredientsJoin.join(BytePackageIngredients_.INGREDIENTS, JoinType.LEFT);

                predicates.add(ingredientsJoin.get(Ingredients_.INGREDIENTS_ID).in(bytePackageSearchDTO.getIngredientList()));
            }

            if (Objects.nonNull(bytePackageSearchDTO.getFromPrice())) {
                    predicates.add(criteriaBuilder.greaterThan(root.get(Product_.PRICE),
                            bytePackageSearchDTO.getFromPrice()));
                }

           if (Objects.nonNull(bytePackageSearchDTO.getToPrice())) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Product_.PRICE),
                            bytePackageSearchDTO.getToPrice()));
                }

           if (Objects.nonNull(bytePackageSearchDTO.getProductCategory()))
                predicates.add(criteriaBuilder.equal(root.get(Product_.PRODUCT_CATEGORY).get(ProductCategory_.CODE), bytePackageSearchDTO.getProductCategory()));

            predicates.add(criteriaBuilder.notEqual(root.get(Product_.STATUS), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<Product> generateSearchCriteria(Status status) {
        return (Specification<Product>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(Product_.STATUS), Status.deleted));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
