package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.AddToCartRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface AddToCartService {
    Object getReferenceData();

    ResponseEntity<Object> addToCart(AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception;

    Object getToCart(AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception;

    Object getCheckoutToCart(AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> removeToCart(Long id, Locale locale) throws Exception;
}
