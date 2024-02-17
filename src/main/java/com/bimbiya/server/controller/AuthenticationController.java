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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationController {

    private AuthenticationServiceImpl authenticationServiceImpl;

    private UserService userService;

    @GetMapping(value = {"/search-reference-data"})
    @CrossOrigin(origins = "*")
    public ResponseEntity<Object> getReferenceData() {
        log.info("Received User Search Reference Data Request {} -");
        return ResponseEntity.status(HttpStatus.OK).body(userService.getReferenceData());
    }

    @PostMapping(value = {"/register"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@Validated({InsertValidation.class}) @RequestBody UserRequestDTO userRequestDTO, Locale locale) throws Exception {
        log.info("Received Auth Register Request {} -", userRequestDTO);
        return userService.saveUser(userRequestDTO, locale, true);
    }

    @PostMapping(value = {"/login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loginUser(@Validated({LoginValidation.class}) @RequestBody RegistrationDTO registrationDTO, Locale locale) {
        log.info("Received Auth Login Request {} -", registrationDTO);
        return authenticationServiceImpl.loginUser(registrationDTO, locale);
    }

    @PostMapping(value = {"/user-login"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loginClient(@Validated({LoginValidation.class}) @RequestBody RegistrationDTO registrationDTO, Locale locale) {
        log.info("Received Auth Login Client Request {} -", registrationDTO);
        return authenticationServiceImpl.loginClient(registrationDTO, locale);
    }

    @PostMapping(value = {"/otp-confirm/{otp}/{userName}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> otpConfirm(@PathVariable Integer otp, @PathVariable String userName, Locale locale) throws Exception {
        log.info("Received Auth OTP Client Request {} -", otp);
        return userService.confirmOtp(otp, userName, locale);
    }

}
