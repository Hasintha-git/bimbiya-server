package com.bimbiya.server.repository;

import com.bimbiya.server.entity.AddToCart;
import com.bimbiya.server.entity.BytePackage;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddToCartRepository extends JpaRepository<AddToCart, Long>, JpaSpecificationExecutor<AddToCart> {

    List<AddToCart> findAllBySystemUserAndStatusNot(SystemUser systemUser, Status status);
//    Long countAllBy();
    AddToCart findBySystemUserAndBpackage(SystemUser systemUser, BytePackage bytePackage);
}