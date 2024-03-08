package com.bimbiya.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class OrderDetailsResponseDTO {
    private Long orderDetailId;
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private int personCount;
    private BigDecimal subTotal;
    private String potion;
}