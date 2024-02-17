package com.bimbiya.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class CheckoutResponseDTO {
    private Long userId;
    private String email;
    private String address;
    private String city;
    private String userName;
    private String fullName;
    private BigDecimal subTotal;
    private BigDecimal deliveryPrice;
    private BigDecimal total;

    private List<AddToCartResponseDTO> cartList;
}