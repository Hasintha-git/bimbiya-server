package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.ProductRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface ProductService {
    Object getReferenceData();

    Object getProductFilterList(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;
    Object getProductFilterListClient(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findProductById(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;
    ResponseEntity<Object> trendingPackagesList(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveProduct(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editProduct(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> deleteProduct(ProductRequestDTO productRequestDTO, Locale locale) throws Exception;
}
