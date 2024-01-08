package com.bimbiya.server.repository;

import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.util.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SystemUser, Long>, JpaSpecificationExecutor<SystemUser> {
    Optional<SystemUser> findByUsername(String userName);
    SystemUser findByUsernameAndStatus(String userName, Status status);
    SystemUser findByUsernameAndStatusNot(String userName,Status status);
    SystemUser findByEmailAndStatusNot(String email,Status status);
    SystemUser findByNicAndStatusNot(String nic,Status status);
    SystemUser findByMobileNoAndStatusNot(String mobile,Status status);

    SystemUser findByUsernameAndStatusNotAndIdNot(String userName,Status status, Long id);
    SystemUser findByEmailAndStatusNotAndIdNot(String email,Status status, Long id);
    SystemUser findByNicAndStatusNotAndIdNot(String nic,Status status, Long id);
    SystemUser findByMobileNoAndStatusNotAndIdNot(String mobile,Status status, Long id);

    SystemUser findByIdAndStatusNot(Long id, Status status);
    SystemUser findByIdAndStatus(Long id, Status status);
    Long countByStatus(Status status);
}
