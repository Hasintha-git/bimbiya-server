package com.bimbiya.server.dto.request;

import com.bimbiya.server.dto.filter.OrderFilterDTO;
import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

/**
 * @author Hasintha_S
 * @date 11/19/2023.
 */

@NoArgsConstructor
@Data
public class OrderRequestDTO {

    @NotNull(message = "Order Id id required", groups = {FindValidation.class, UpdateValidation.class})
    private Long orderId;

    @NotNull(message = "User Id required", groups = {UpdateValidation.class, InsertValidation.class})
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date orderDate;

    @NotNull(message = "Total required", groups = {UpdateValidation.class})
    private BigDecimal total;

    private BigDecimal deliveryPrice;

    private List<ProductRequestDTO> product;

    private LocalTime scheduledTime;

    @NotBlank(message = "Status required", groups = {UpdateValidation.class})
    private String status;

    private String activeUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;


    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

    private OrderFilterDTO orderFilterDTO;
}
