package com.bimbiya.server.dto.request;

import com.bimbiya.server.validators.DeleteValidation;
import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;

@NoArgsConstructor
@Data
public class AddToCartRequestDTO {

    @NotNull(message = "Cart Id required", groups = {  DeleteValidation.class, UpdateValidation.class })
    private Long cartId;

    @NotNull(message = "QTY required", groups = {  InsertValidation.class })
    private Integer qty;

    @NotNull(message = "Package Id required", groups = {  InsertValidation.class })
    private Long packageId;

    @NotBlank(message = "User name required", groups = {  InsertValidation.class, FindValidation.class})
    private String userName;

    private Integer personCount;

    private LocalTime scheduledTime;

    private String status;
    private String activeUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;
}