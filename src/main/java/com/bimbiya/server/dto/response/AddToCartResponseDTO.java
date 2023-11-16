package com.bimbiya.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class AddToCartResponseDTO {
    private Long cartId;
    private Integer qty;
    private Long packageId;
    private String mealName;
    private Long userId;
    private String userName;
    private String status;
    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;
}