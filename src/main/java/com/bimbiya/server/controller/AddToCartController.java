package com.bimbiya.server.controller;

import com.bimbiya.server.dto.request.AddToCartRequestDTO;
import com.bimbiya.server.service.AddToCartService;
import com.bimbiya.server.util.EndPoint;
import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/cart")
@Log4j2
@CrossOrigin(origins = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AddToCartController {

    private AddToCartService addToCartService;

//    @GetMapping(value = EndPoint.INGREDIENT_REQUEST_SEARCH_DATA)
//    @CrossOrigin(origins = "*")
//    public ResponseEntity<Object> getReferenceData() {
//        log.info("Received Ingredient Search Reference Data Request {} -");
//        return ResponseEntity.status(HttpStatus.OK).body(ingredientService.getReferenceData());
//    }

    @PostMapping(value = {EndPoint.GET_TO_CART}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public Object findCartList(@Validated({FindValidation.class})
                               @RequestBody AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception {
        log.info("Received Get To Cart find List Request {} -", addToCartRequestDTO);
        return addToCartService.getToCart(addToCartRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.GET_CHECKOUT_TO_CART}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public Object checkoutCartList(@Validated({FindValidation.class})
                                   @RequestBody AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception {
        log.info("Received Get To Cart find List Request {} -", addToCartRequestDTO);
        return addToCartService.getCheckoutToCart(addToCartRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.ADD_TO_CART}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addToCart(@Validated({InsertValidation.class})
                                            @RequestBody AddToCartRequestDTO addToCartRequestDTO, Locale locale) throws Exception {
        log.info("Received Add to cart add List Request {} -", addToCartRequestDTO);
        return addToCartService.addToCart(addToCartRequestDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.REMOVE_TO_CART + "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> removeToCart(@PathVariable Long id, Locale locale) throws Exception {
        log.info("Received Remove to cart Request {} -", id);
        return addToCartService.removeToCart(id, locale);
    }
}
