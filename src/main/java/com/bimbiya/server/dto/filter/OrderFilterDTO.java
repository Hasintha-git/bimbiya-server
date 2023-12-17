package com.bimbiya.server.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
public class OrderFilterDTO {
    private Long userId;
    private Date orderDate;
    private String status;
}
