package com.bimbiya.server.repository;

import com.bimbiya.server.entity.BytePackage;
import com.bimbiya.server.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>, JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findAllByBytePackage(BytePackage bytePackage);
}