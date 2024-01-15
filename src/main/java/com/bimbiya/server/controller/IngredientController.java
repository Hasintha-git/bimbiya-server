package com.bimbiya.server.controller;

import com.bimbiya.server.dto.filter.IngredientSearchDTO;
import com.bimbiya.server.dto.request.IngredientsRequestDTO;
import com.bimbiya.server.service.IngredientService;
import com.bimbiya.server.util.EndPoint;
import com.bimbiya.server.validators.DeleteValidation;
import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/ingredient")
@Log4j2
@CrossOrigin(origins = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientController {

    private IngredientService ingredientService;

    @GetMapping(value = EndPoint.INGREDIENT_REQUEST_SEARCH_DATA)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received Ingredient Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.INGREDIENT_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getIngredientFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String ingredientsName,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        IngredientsRequestDTO ingredientsRequestDTO = new IngredientsRequestDTO();
        IngredientSearchDTO ingredientSearchDTO= new IngredientSearchDTO();
        ingredientSearchDTO.setIngredientsName(ingredientsName);
        ingredientSearchDTO.setStatus(status);

        ingredientsRequestDTO.setIngredientSearchDTO(ingredientSearchDTO);

        ingredientsRequestDTO.setPageNumber(start);
        ingredientsRequestDTO.setPageSize(limit);
        ingredientsRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            ingredientsRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received Ingredient get Filtered List Request {} -", ingredientsRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ingredientService.getIngredientFilterList(ingredientsRequestDTO, locale));
    }

    @PostMapping(value = {EndPoint.INGREDIENT_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findIngredient(@Validated({ FindValidation.class})
            @RequestBody IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        log.info("Received Ingredient find List Request {} -", ingredientsRequestDTO);
        return ingredientService.findIngredientById(ingredientsRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.INGREDIENT_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addIngredient( @Validated({InsertValidation.class})
            @RequestBody IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        log.info("Received Ingredient add List Request {} -", ingredientsRequestDTO);
        return ingredientService.saveIngredient(ingredientsRequestDTO, locale);
    }

    @PutMapping(value = {EndPoint.INGREDIENT_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateIngredient(@Validated({ UpdateValidation.class})
            @RequestBody IngredientsRequestDTO ingredientsRequestDTO, Locale locale) throws Exception {
        log.info("Received Ingredient update List Request {} -", ingredientsRequestDTO);
        return ingredientService.editIngredient(ingredientsRequestDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.INGREDIENT_REQUEST_MGT+"/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteIngredient(@Validated({ DeleteValidation.class})
                                                       @PathVariable Long id, Locale locale) throws Exception {
        log.info("Received Ingredient delete List Request {} -", id);
        return ingredientService.deleteIngredient(id, locale);
    }
}
