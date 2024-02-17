package com.bimbiya.server.repository;

import com.bimbiya.server.entity.AddToCart;
import com.bimbiya.server.entity.Product;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddToCartRepository extends JpaRepository<AddToCart, Long>, JpaSpecificationExecutor<AddToCart> {

    List<AddToCart> findAllBySystemUserAndStatusNot(SystemUser systemUser, Status status);

    AddToCart findAddToCartByBpackage_PackageId(Long id);

    //    Long countAllBy();
    AddToCart findBySystemUserAndBpackage(SystemUser systemUser, Product product);
}