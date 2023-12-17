package com.bimbiya.server.dto.request;

import com.bimbiya.server.validators.DeleteValidation;
import com.bimbiya.server.validators.FindValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Hasintha_S
 * @date 11/19/2023.
 */

@NoArgsConstructor
@Data
public class OrderDetailRequestDTO {

    @NotNull(message = "Order Id id required", groups = { FindValidation.class, DeleteValidation.class, UpdateValidation.class , InsertValidation.class })
    private Long orderId;

    @NotNull(message = "Package Id required", groups = { UpdateValidation.class , InsertValidation.class })
    private Long packageId;

    @NotNull(message = "Quantity required", groups = { UpdateValidation.class , InsertValidation.class })
    private int quantity;

    @NotNull(message = "Unit Price required", groups = {  InsertValidation.class })
    private BigDecimal unitPrice;

    @NotNull(message = "Sub Total required", groups = {  InsertValidation.class })
    private BigDecimal subTotal;

}
