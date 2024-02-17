package com.bimbiya.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@Data
public class AddToCartResponseDTO {
    private Long cartId;
    private Integer qty;
    private Long packageId;
    private String mealName;
    private BigDecimal price;
    private Long userId;
    private String email;
    private Integer personCount;
    private String address;
    private String city;
    private String image;
    private String userName;
    private String fullName;
    private String status;
    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;
}