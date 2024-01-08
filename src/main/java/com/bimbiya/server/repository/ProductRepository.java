package com.bimbiya.server.repository;

import com.bimbiya.server.entity.Product;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Product findByPackageIdAndStatusNot(Long id, Status status);
    Product findByPackageIdAndStatus(Long id, Status status);
    Product findByProductNameAndStatusNot(String name, String status);

    Optional<Product> findByProductNameAndStatusNotAndPackageIdNot(String name, String status, Long id);

}
