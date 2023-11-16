package com.bimbiya.server.controller;

import com.bimbiya.server.dto.request.RegistrationDTO;
import com.bimbiya.server.dto.request.UserRequestDTO;
import com.bimbiya.server.service.UserService;
import com.bimbiya.server.service.impl.AuthenticationServiceImpl;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.LoginValidation;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    private AuthenticationServiceImpl authenticationServiceImpl;

    private UserService userService;

    @PostMapping(value = {"/register"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@Validated({InsertValidation.class}) @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.debug("Received Auth Register Request {} -", userRequestDTO);
        return userService.saveUser(userRequestDTO,locale);
    }

    @PostMapping(value = {"/login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loginUser(@Validated({LoginValidation.class}) @RequestBody RegistrationDTO registrationDTO, Locale locale) {
        log.debug("Received Auth Login Request {} -", registrationDTO);
        return authenticationServiceImpl.loginUser(registrationDTO,locale);
    }

}
