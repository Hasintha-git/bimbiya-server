package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.OrderRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface OrderService {
    Object getReferenceData();

    Object getOrderFilterList(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findOrderById(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> orderProcessingUpdate(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception;
    ResponseEntity<Object> newOrder(OrderRequestDTO orderRequestDTO, Locale locale) throws Exception;

}
