package com.bimbiya.server.controller;

import com.bimbiya.server.dto.filter.OrderFilterDTO;
import com.bimbiya.server.dto.request.OrderRequestDTO;
import com.bimbiya.server.service.OrderService;
import com.bimbiya.server.util.EndPoint;
import com.bimbiya.server.validators.FindValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@RestController
@RequestMapping("/order")
@Log4j2
@CrossOrigin(origins = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private OrderService orderService;

    @GetMapping(value = EndPoint.ORDER_REQUEST_SEARCH_DATA)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received Ingredient Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.ORDER_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getIngredientFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "MM/dd/yyyy") Date orderDate,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        OrderFilterDTO orderFilterDTO= new OrderFilterDTO();
        orderFilterDTO.setUserId(userId);
            orderFilterDTO.setStatus(status);
        orderFilterDTO.setOrderDate(orderDate);

        orderRequestDTO.setOrderFilterDTO(orderFilterDTO);

        orderRequestDTO.setPageNumber(start);
        orderRequestDTO.setPageSize(limit);
        orderRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            orderRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received ORDER get Filtered List Request {} -", orderRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderFilterList(orderRequestDTO, locale));
    }

    @PostMapping(value = {EndPoint.ORDER_REQUEST_FIND}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findOrder(@Validated({ FindValidation.class})
                                                 @RequestBody OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        log.info("Received Order find List Request {} -", orderRequestDTO);
        return orderService.findOrderById(orderRequestDTO, locale);
    }

    @PutMapping(value = {EndPoint.ORDER_REQUEST_UPDATE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> orderStatusUpdate(@RequestBody OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        log.info("Received Order find List Request {} -", orderRequestDTO);
        return orderService.orderProcessingUpdate(orderRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.PLACE_ORDER}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO, Locale locale) throws Exception {
        log.info("Received Order Place Request {} -", orderRequestDTO);
        return orderService.newOrder(orderRequestDTO, locale);
    }
}
