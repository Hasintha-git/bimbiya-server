package com.bimbiya.server.service;

import com.bimbiya.server.dto.request.EmailSendDTO;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

public interface NotificationService {
    ResponseEntity<Object> sendEmail(EmailSendDTO emailSendDTO, Locale locale) throws Exception;
    String encript(EmailSendDTO emailSendDTO, Locale locale) throws Exception;


}
