package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.UserRequestDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface UserService {

    Object getReferenceData();

    Object getUserFilterList(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> findUserById(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> saveUser(UserRequestDTO userRequestDTO, Locale locale) throws Exception;

    ResponseEntity<Object> editUser(UserRequestDTO userRequestDTO, Locale locale);

    ResponseEntity<Object> deleteUser(UserRequestDTO userRequestDTO, Locale locale);
}
