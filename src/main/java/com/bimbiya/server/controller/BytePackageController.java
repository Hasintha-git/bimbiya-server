package com.bimbiya.server.controller;

import com.bimbiya.server.dto.filter.BytePackageSearchDTO;
import com.bimbiya.server.dto.request.BytePackageRequestDTO;
import com.bimbiya.server.service.BytePackageService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/byte-package")
@Log4j2
@CrossOrigin(origins = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BytePackageController {

    private BytePackageService bytePackageService;

    @GetMapping(value = EndPoint.BYTE_PACKAGE_REQUEST_SEARCH_DATA)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received Byte Package Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(bytePackageService.getReferenceData());
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
            @RequestParam(required = false) BigDecimal price,
            @RequestParam(required = false) List<String> portion,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        BytePackageRequestDTO bytePackageRequestDTO = new BytePackageRequestDTO();
        BytePackageSearchDTO searchDTO= new BytePackageSearchDTO();
        searchDTO.setMealName(mealName);
        searchDTO.setPrice(price);
        searchDTO.setPortionList(portion);
        searchDTO.setStatus(status);

        bytePackageRequestDTO.setBytePackageSearchDTO(searchDTO);

        bytePackageRequestDTO.setPageNumber(start);
        bytePackageRequestDTO.setPageSize(limit);
        bytePackageRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            bytePackageRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received Byte Package get Filtered List Request {} -", bytePackageRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(bytePackageService.getBytePackageFilterList(bytePackageRequestDTO, locale));
    }

    @PostMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findBytePackage(@Validated({ FindValidation.class})
            @RequestBody BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package find List Request {} -", bytePackageRequestDTO);
        return bytePackageService.findBytePackageById(bytePackageRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addBytePackage( @Validated({InsertValidation.class})
            @RequestBody BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package add List Request {} -", bytePackageRequestDTO);
        return bytePackageService.saveBytePackage(bytePackageRequestDTO, locale);
    }

    @PutMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateBytePackage(@Validated({ UpdateValidation.class})
            @RequestBody BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package update List Request {} -", bytePackageRequestDTO);
        return bytePackageService.editBytePackage(bytePackageRequestDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.BYTE_PACKAGE_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<Object> deleteBytePackage(@Validated({ DeleteValidation.class})
                                                        @RequestBody BytePackageRequestDTO bytePackageRequestDTO, Locale locale) throws Exception {
        log.info("Received Byte Package delete List Request {} -", bytePackageRequestDTO);
        return bytePackageService.deleteBytePackage(bytePackageRequestDTO, locale);
    }
}
