package com.bimbiya.server.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ByteIngredientsResponseDTO {
    private Long ingredientsId;
    private String ingredientsName;
    private String status;
}