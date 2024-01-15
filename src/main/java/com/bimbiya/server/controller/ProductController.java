package com.bimbiya.server.controller;

import com.bimbiya.server.dto.filter.BytePackageSearchDTO;
import com.bimbiya.server.dto.request.ProductRequestDTO;
import com.bimbiya.server.service.ProductService;
import com.bimbiya.server.util.EndPoint;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/product")
@Log4j2
@CrossOrigin(origins = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ProductController {

    private ProductService productService;

    @GetMapping(value = EndPoint.BYTE_PACKAGE_REQUEST_SEARCH_DATA)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received Byte Package Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(productService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getBytePackageFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String mealName,
            @RequestParam(required = false) BigDecimal toPrice,
            @RequestParam(required = false) BigDecimal fromPrice,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) List<String> portion,
            @RequestParam(required = false) List<Long> ingredientList,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        BytePackageSearchDTO searchDTO= new BytePackageSearchDTO();
        searchDTO.setMealName(mealName);
        searchDTO.setToPrice(toPrice);
        searchDTO.setFromPrice(fromPrice);
        searchDTO.setPortionList(portion);
        searchDTO.setIngredientList(ingredientList);
        searchDTO.setStatus(status);
        searchDTO.setProductCategory(productCategory);

        productRequestDTO.setBytePackageSearchDTO(searchDTO);

        productRequestDTO.setPageNumber(start);
        productRequestDTO.setPageSize(limit);
        productRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            productRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received Byte Package get Filtered List Request {} -", productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductFilterList(productRequestDTO, locale));
    }

    @GetMapping(value = {EndPoint.BYTE_PACKAGE_CLIENT_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getBytePackageFilteredListForClient(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String mealName,
            @RequestParam(required = false) BigDecimal toPrice,
            @RequestParam(required = false) BigDecimal fromPrice,
            @RequestParam(required = false) String productCategory,
            @RequestParam(required = false) List<String> portion,
            @RequestParam(required = false) List<Long> ingredientList,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        BytePackageSearchDTO searchDTO= new BytePackageSearchDTO();
        searchDTO.setMealName(mealName);
        searchDTO.setToPrice(toPrice);
        searchDTO.setFromPrice(fromPrice);
        searchDTO.setPortionList(portion);
        searchDTO.setIngredientList(ingredientList);
        searchDTO.setStatus(status);
        searchDTO.setProductCategory(productCategory);

        productRequestDTO.setBytePackageSearchDTO(searchDTO);

        productRequestDTO.setPageNumber(start);
        productRequestDTO.setPageSize(limit);
        productRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            productRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received Byte Package get Filtered List Request {} -", productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.getProductFilterListClient(productRequestDTO, locale));
    }

    @PostMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findBytePackage(@Validated({ FindValidation.class})
            @RequestBody ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package find List Request {} -", productRequestDTO);
        return productService.findProductById(productRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addBytePackage(@Validated({InsertValidation.class})
            @RequestBody ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package add List Request {} -", productRequestDTO);
        return productService.saveProduct(productRequestDTO, locale);
    }

    @PutMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateBytePackage(@Validated({ UpdateValidation.class})
            @RequestBody ProductRequestDTO productRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package update List Request {} -", productRequestDTO);
        return productService.editProduct(productRequestDTO, locale);
    }

    @DeleteMapping(value = "/v1/admin-byte/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<Object> deleteBytePackage(@PathVariable Long id, Locale locale) throws Exception {
        ProductRequestDTO productRequestDTO = new ProductRequestDTO();
        productRequestDTO.setPackageId(id);
        log.info("Received Byte Package delete List Request {} -", productRequestDTO);
        return productService.deleteProduct(productRequestDTO, locale);
    }
}
