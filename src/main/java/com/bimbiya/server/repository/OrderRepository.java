package com.bimbiya.server.repository;

import com.bimbiya.server.entity.Order;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    List<Order> findAllBySystemUserAndStatus(SystemUser systemUser, Status status);
    List<Order> findAllBySystemUser(SystemUser systemUser);
    List<Order> findAllByOrderDateAndStatus(Date date, Status status);
    Optional<Order> findById(Long id);
    Long countAllByOrderDateAndStatus(Date date, Status status);
    Long countAllByOrderDateAndStatusNot(Date date, Status status);
    Long countAllByOrderDate(Date date);
    Long countAllByStatus(Status status);
    Long countAllBySystemUserAndStatus(SystemUser systemUser, Status status);
    Long countAllBySystemUserAndStatusNot(SystemUser systemUser, Status status);
    Long countAllBySystemUser(SystemUser systemUser);
}