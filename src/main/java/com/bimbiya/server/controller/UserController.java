package com.bimbiya.server.controller;

import com.bimbiya.server.dto.request.UserRequestDTO;
import com.bimbiya.server.dto.filter.UserRequestSearchDTO;
import com.bimbiya.server.service.UserService;
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
@RequestMapping("/user")
@Log4j2
@CrossOrigin(origins = "*")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private UserService userService;

    @GetMapping(value = EndPoint.USER_REQUEST_SEARCH_DATA)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received User Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getReferenceData());
    }
    @GetMapping(value = {EndPoint.USER_REQUEST_FILTER_LIST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getUserFilteredList(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String nic,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String userRole,
            @RequestParam(required = false) String mobileNo,
            @RequestParam(required = false) Boolean full,
            @RequestParam(required = false) Boolean dataTable,
            @RequestParam(required = false) Integer draw, Locale locale) throws Exception {

        UserRequestDTO userRequestDTO = new UserRequestDTO();
        UserRequestSearchDTO userRequestSearchDTO= new UserRequestSearchDTO();
        userRequestSearchDTO.setUserName(username);
        userRequestSearchDTO.setNic(nic);
        userRequestSearchDTO.setEmail(email);
        userRequestSearchDTO.setUserRole(userRole);
        userRequestSearchDTO.setMobileNo(mobileNo);
        userRequestSearchDTO.setStatus(status);

        userRequestDTO.setUserRequestSearchDTO(userRequestSearchDTO);

        userRequestDTO.setPageNumber(start);
        userRequestDTO.setPageSize(limit);
        userRequestDTO.setSortColumn(sortBy);
        if (Objects.nonNull(order) && !order.isEmpty()) {
            userRequestDTO.setSortDirection(order.toUpperCase());
        }
        log.info("Received User get Filtered List Request {} -", userRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserFilterList(userRequestDTO, locale));
    }

    @PostMapping(value = {EndPoint.USER_REQUEST_FIND_ID}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> findUser(@Validated({ FindValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.info("Received User find List Request {} -", userRequestDTO);
        return userService.findUserById(userRequestDTO, locale);
    }

    @PostMapping(value = {EndPoint.USER_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> addUser( @Validated({InsertValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.info("Received User add List Request {} -", userRequestDTO);
        return userService.saveUser(userRequestDTO, locale);
    }

    @PutMapping(value = {EndPoint.USER_REQUEST_MGT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> updateUser(@Validated({ UpdateValidation.class})
            @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.info("Received User update List Request {} -", userRequestDTO);
        return userService.editUser(userRequestDTO, locale);
    }

    @DeleteMapping(value = {EndPoint.USER_REQUEST_MGT+ "/{id}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> deleteUser(@Validated({ DeleteValidation.class})
                                                 @PathVariable Long id, Locale locale) throws Exception {
        log.info("Received User delete List Request {} -", id);
        UserRequestDTO userRequestDTO=new UserRequestDTO();
        userRequestDTO.setId(id);
        return userService.deleteUser(userRequestDTO, locale);
    }
}
