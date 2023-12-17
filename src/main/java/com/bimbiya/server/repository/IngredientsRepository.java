package com.bimbiya.server.repository;

import com.bimbiya.server.entity.Ingredients;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, Long> , JpaSpecificationExecutor<Ingredients> {
    Ingredients findIngredientsByIngredientsIdAndStatusCodeNot(Long id, Status status);
    Ingredients findIngredientsByIngredientsNameAndStatusCodeNot(String name, Status status);
    List<Ingredients> findAllByStatusCode(Status status);
}
