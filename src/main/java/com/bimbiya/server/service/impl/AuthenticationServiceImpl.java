package com.bimbiya.server.service.impl;

import com.bimbiya.server.dto.request.RegistrationDTO;
import com.bimbiya.server.dto.response.LoginResponseDTO;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.mapper.ResponseGenerator;
import com.bimbiya.server.repository.UserRepository;
import com.bimbiya.server.util.MessageConstant;
import com.bimbiya.server.util.ResponseCode;
import com.bimbiya.server.util.Utility;
import com.bimbiya.server.util.enums.Status;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Log4j2
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl {

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private TokenServiceImpl tokenServiceImpl;

    private ResponseGenerator responseGenerator;

    private final long accessTokenExpirationMs = 3600000;
    private final long refreshTokenExpirationMs = 604800000;

    public ResponseEntity<Object> loginUser(RegistrationDTO registrationDTO, Locale locale) {
        try {
            SystemUser systemUser = Optional.ofNullable(userRepository.findByUsernameAndStatus(registrationDTO.getUsername(), Status.active)).orElse(null);

            if (Objects.isNull(systemUser)) {
                return responseGenerator.generateErrorResponse(registrationDTO, HttpStatus.NOT_FOUND,
                        ResponseCode.GET_SUCCESS, MessageConstant.USER_NOT_FOUND, new
                                Object[]{registrationDTO.getUsername()}, locale);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registrationDTO.getUsername(), registrationDTO.getPassword())
            );

            String accessToken = tokenServiceImpl.generateJwtToken(authentication);
            String refreshToken = tokenServiceImpl.generateJwtToken(authentication);
            Date accessTokenExpireDate =
                    new Date(Utility.getSystemDate().getTime() + accessTokenExpirationMs * 1000);

            Date refreshTokenExpireDate =
                    new Date(Utility.getSystemDate().getTime() + refreshTokenExpirationMs);
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO(systemUser, accessToken, refreshToken,accessTokenExpireDate,refreshTokenExpireDate);

            // Return the JWT tokens in the response header
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Refresh-Token", refreshToken)
                    .body(responseGenerator.generateSuccessResponse(
                            registrationDTO, HttpStatus.OK, ResponseCode.GET_SUCCESS,
                            MessageConstant.SUCCESSFULLY_GET, locale, loginResponseDTO)
                    );
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
