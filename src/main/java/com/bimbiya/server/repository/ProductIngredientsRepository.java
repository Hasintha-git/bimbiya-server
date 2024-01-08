package com.bimbiya.server.repository;

import com.bimbiya.server.entity.Product;
import com.bimbiya.server.entity.BytePackageIngredients;
import com.bimbiya.server.entity.BytePackageIngredientsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductIngredientsRepository extends JpaRepository<BytePackageIngredients, BytePackageIngredientsId>, JpaSpecificationExecutor<BytePackageIngredients> {

    List<BytePackageIngredients> findAllByProduct(Product product);
//    List<BytePackageIngredients> findAllByProduct(Ingredients ingredients);
    void deleteAllByProduct(Product product);
}