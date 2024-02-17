package com.bimbiya.server.dto.request;

import com.bimbiya.server.dto.filter.UserRequestSearchDTO;
import com.bimbiya.server.validators.DeleteValidation;
import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Data
public class UserRequestDTO {
    @NotNull(message = "User id required", groups = { FindValidation.class, DeleteValidation.class,
            UpdateValidation.class })
    private Long id;

    @NotBlank(message = "User name required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String username;

    @NotBlank(message = "Password required", groups = {  InsertValidation.class })
    private String password;

    @NotBlank(message = "User role required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String userRole;

    @NotBlank(message = "Full name required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String fullName;

    @NotBlank(message = "Nic required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String nic;

    @NotBlank(message = "Email required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String email;

    @NotBlank(message = "Mobile no required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String mobileNo;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date dateOfBirth;

    @NotBlank(message = "Address required", groups = {InsertValidation.class, UpdateValidation.class})
    private String address;

    @NotBlank(message = "City required", groups = {InsertValidation.class, UpdateValidation.class})
    private String city;

    @NotBlank(message = "Status required", groups = {InsertValidation.class, UpdateValidation.class})
    private String status;

    @NotBlank(message = "District required", groups = {InsertValidation.class, UpdateValidation.class})
    private String district;

    private String pwStatus;

    private Long attempt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastLoggedDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date passwordExpireDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    private String createdUser;

    private String lastUpdatedUser;

    private String activeUserName;

    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

    private UserRequestSearchDTO userRequestSearchDTO;
}
