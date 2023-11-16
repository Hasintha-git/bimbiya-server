package com.bimbiya.server.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class BytePackageSearchDTO {
    private String mealName;
    private BigDecimal price;
    List<String> portionList;
    private String status;
}
