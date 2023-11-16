package com.bimbiya.server.repository;

import com.bimbiya.server.entity.BytePackage;
import com.bimbiya.server.entity.BytePackageIngredients;
import com.bimbiya.server.entity.BytePackageIngredientsId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BytePackageIngredientsRepository extends JpaRepository<BytePackageIngredients, BytePackageIngredientsId>, JpaSpecificationExecutor<BytePackageIngredients> {

    List<BytePackageIngredients> findAllByBytePackage(BytePackage bytePackage);
//    List<BytePackageIngredients> findAllByIngredients(Ingredients ingredients);
    void deleteAllByBytePackage(BytePackage bytePackage);
}