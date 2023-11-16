package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.IngredientsRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface IngredientService {
    Object getReferenceData();

    Object getIngredientFilterList(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findIngredientById(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveIngredient(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editIngredient(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> deleteIngredient(IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception;
}
