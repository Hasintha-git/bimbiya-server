package com.bimbiya.server.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class BytePackageSearchDTO {
    private String mealName;
    private BigDecimal toPrice;
    private BigDecimal fromPrice;
    List<String> portionList;
    List<Long> ingredientList;
    private String status;
    private String productCategory;
}
