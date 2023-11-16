package com.bimbiya.server.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class BytePackageResponseDTO {
    private Long packageId;
    private String mealName;
    private String description;
    private BigDecimal price;
    private String portion;
    private String portionDescription;
    private String status;
    private String statusDescription;
    private String createdUser;
    private String lastUpdatedUser;
    private Date createdTime;
    private Date lastUpdatedTime;

    private List<ByteIngredientsResponseDTO> byteIngredientsResponseDTOList;
}
