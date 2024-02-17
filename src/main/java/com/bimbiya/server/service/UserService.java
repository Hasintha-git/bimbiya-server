package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.UserRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface UserService {

    Object getReferenceData();

    Integer sentOtp(String email, Locale locale) throws Exception;

    ResponseEntity<Object> confirmOtp(Integer otp, String userName, Locale locale) throws Exception;

    Object getUserFilterList(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findUserById(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> forDashboard(Locale locale) throws Exception;

    ResponseEntity<Object> saveUser(UserRequestDTO userRequestDTO, Locale locale, boolean client) throws Exception;

    ResponseEntity<Object> editUser(UserRequestDTO userRequestDTO, Locale locale);

    ResponseEntity<Object> forgetPassword(UserRequestDTO userRequestDTO, Locale locale);
    ResponseEntity<Object> lockUser(UserRequestDTO userRequestDTO, Locale locale);
    ResponseEntity<Object> unlockUser(UserRequestDTO userRequestDTO, Locale locale);

    ResponseEntity<Object> deleteUser(UserRequestDTO userRequestDTO, Locale locale);
}
