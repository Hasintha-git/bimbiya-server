package com.bimbiya.server.repository;

import com.bimbiya.server.entity.UserRole;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByCode(String code);
    List<UserRole> findAllByStatusCodeNot(Status status);
    List<UserRole> findAllByStatusCode(Status status);

    UserRole findByCodeAndStatusCode(String code, Status status);

}
