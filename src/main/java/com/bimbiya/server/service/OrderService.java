package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.IngredientsRequestDTO;
import com.bimbiya.server.dto.request.OrderRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface OrderService {
    Object getReferenceData();

    Object getOrderFilterList(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findOrderById(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> orderProcessingUpdate(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;

}
