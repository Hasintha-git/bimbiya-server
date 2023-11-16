package com.bimbiya.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String userRole;
    private String userRoleDescription;
    private String fullName;
    private String nic;
    private String email;
    private String mobileNo;
    private Date dateOfBirth;
    private String address;
    private String city;
    private String status;
    private String pwStatus;
    private String statusDescription;
    private String passwordStatusDescription;
    private int attempt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastLoggedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date passwordExpireDate;
    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;
    public UserResponseDTO(Long id) {
        this.id = id;
    }

    public UserResponseDTO(String username) {
        this.username = username;
    }
}
