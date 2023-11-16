package com.bimbiya.server.repository.specifications;

import com.bimbiya.server.dto.filter.IngredientSearchDTO;
import com.bimbiya.server.entity.Ingredients;
import com.bimbiya.server.entity.Ingredients_;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class IngredientSpecification {

    public Specification<Ingredients> generateSearchCriteria(IngredientSearchDTO ingredientSearchDTO) {
        return (Specification<Ingredients>)(root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(ingredientSearchDTO.getIngredientsName()) )
                predicates.add(criteriaBuilder.equal(root.get(Ingredients_.INGREDIENTS_NAME), ingredientSearchDTO.getIngredientsName()));

            if (Objects.nonNull(ingredientSearchDTO.getStatus()) && !ingredientSearchDTO.getStatus().isEmpty())
                predicates.add(criteriaBuilder.equal(root.get(Ingredients_.STATUS_CODE), Status.valueOf(ingredientSearchDTO.getStatus())));

            predicates.add(criteriaBuilder.notEqual(root.get(Ingredients_.STATUS_CODE), Status.deleted));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public Specification<Ingredients> generateSearchCriteria(Status status) {
        return (Specification<Ingredients>) (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get(Ingredients_.STATUS_CODE), status));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
