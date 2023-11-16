package com.bimbiya.server.dto.request;

import com.bimbiya.server.validators.LoginValidation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class RegistrationDTO {

    @NotBlank(message = "User name required", groups = {  LoginValidation.class })
    private String username;

    @NotBlank(message = "Password required", groups = {  LoginValidation.class })
    private String password;
}
