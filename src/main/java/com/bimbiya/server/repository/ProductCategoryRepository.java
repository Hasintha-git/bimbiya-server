package com.bimbiya.server.repository;

import com.bimbiya.server.entity.ProductCategory;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long>, JpaSpecificationExecutor<ProductCategory> {
    ProductCategory findByCategoryIdAndStatusNot(Long id, Status status);
    ProductCategory findByCodeAndStatus(String code, Status status);
    List<ProductCategory> findAllByStatus(Status status);

}
