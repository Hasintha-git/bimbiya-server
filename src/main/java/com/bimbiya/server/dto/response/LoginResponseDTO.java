package com.bimbiya.server.dto.response;

import com.bimbiya.server.entity.SystemUser;

import java.util.Date;

public class LoginResponseDTO {

    private SystemUser user;
    private String accessToken;

    private String refreshToken;

    private Date accessTokenExpireDate;

    private Date refreshTokenExpireDate;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(SystemUser user, String accessToken, String refreshToken, Date accessTokenExpireDate, Date refreshTokenExpireDate) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireDate = accessTokenExpireDate;
        this.refreshTokenExpireDate = refreshTokenExpireDate;
    }

    public LoginResponseDTO(SystemUser user, String accessToken, String refreshToken) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

}
