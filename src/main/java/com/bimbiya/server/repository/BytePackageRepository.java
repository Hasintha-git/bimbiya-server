package com.bimbiya.server.repository;

import com.bimbiya.server.entity.BytePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BytePackageRepository extends JpaRepository<BytePackage, Long>, JpaSpecificationExecutor<BytePackage> {
    BytePackage findByPackageIdAndStatusNot(Long id, String status);
    BytePackage findByMealNameAndStatusNot(String name, String status);

    Optional<BytePackage> findByMealNameAndStatusNotAndPackageIdNot(String name, String status, Long id);

}
