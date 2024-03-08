package com.bimbiya.server.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class OrderResponseDTO {
    private Long orderId;
    private Long userId;


    private String username;
    private String fullName;
    private String email;
    private String mobileNo;
    private String address;
    private String city;
    private String scheduleTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date orderDate;
    private BigDecimal totalAmount;
    private String status;
    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    private List<OrderDetailsResponseDTO> orderDetails;
}