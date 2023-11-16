package com.bimbiya.server.dto.filter;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class IngredientSearchDTO {
    private String ingredientsName;
    private String status;
}
