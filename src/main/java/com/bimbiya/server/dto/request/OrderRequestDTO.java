package com.bimbiya.server.dto.request;

import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Hasintha_S
 * @date 11/19/2023.
 */

@NoArgsConstructor
@Data
public class OrderRequestDTO {

    @NotNull(message = "Order Id id required", groups = { FindValidation.class, UpdateValidation.class })
    private Long orderId;

    @NotNull(message = "User Id required", groups = { UpdateValidation.class , InsertValidation.class })
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date orderDate;

    @NotNull(message = "Sub Total required", groups = {  InsertValidation.class })
    private BigDecimal totAmount;

    @NotBlank(message = "Status required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String status;

    private String activeUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

}
