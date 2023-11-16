package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.BytePackageRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface BytePackageService {
    Object getReferenceData();

    Object getBytePackageFilterList(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findBytePackageById(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception;
    ResponseEntity<Object> trendingPackagesList(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveBytePackage(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editBytePackage(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> deleteBytePackage(BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception;
}
