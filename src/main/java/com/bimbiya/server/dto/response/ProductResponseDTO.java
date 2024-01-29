package com.bimbiya.server.dto.response;


import com.bimbiya.server.dto.SimpleBaseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class ProductResponseDTO {
    private Long packageId;
    private String mealName;
    private String description;
    private BigDecimal price;
    private String productCategory;
    private String portion;
    private String portionDescription;
    private String status;
    private String statusDescription;
    private String createdUser;
    private String lastUpdatedUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;
    private String img;
    private List<Long> ingredientList;
    private List<String> ingredientNameList;
    private List<SimpleBaseDTO> ingredients;
}
