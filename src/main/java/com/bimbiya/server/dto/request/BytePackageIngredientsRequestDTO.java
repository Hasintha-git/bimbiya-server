package com.bimbiya.server.dto.request;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Data
public class BytePackageIngredientsRequestDTO {
    @NotNull(message = "Ingredients id required")
    private Long ingredientsId;

    @NotNull(message = "Package id required")
    private Long packageId;

}