package com.bimbiya.server.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class LoginResponseDTO {

    private Long userId;
    private String userName;
    private String accessToken;

    private String refreshToken;

    private Date accessTokenExpireDate;

    private Date refreshTokenExpireDate;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(String accessToken, String refreshToken, Date accessTokenExpireDate, Date refreshTokenExpireDate) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
        this.refreshTokenExpireDate = refreshTokenExpireDate;
    }

    public LoginResponseDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
