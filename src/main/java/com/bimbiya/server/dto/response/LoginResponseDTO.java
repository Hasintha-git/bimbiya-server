package com.bimbiya.server.dto.response;

import com.bimbiya.server.entity.SystemUser;

public class LoginResponseDTO {

    private SystemUser user;
    private String jwt;

    public LoginResponseDTO() {
        super();
    }

    public LoginResponseDTO(SystemUser user, String jwt) {
        this.user = user;
        this.jwt = jwt;
    }

    public SystemUser getUser() {
        return user;
    }

    public void setUser(SystemUser user) {
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
