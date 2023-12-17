package com.bimbiya.server.dto.request;

import com.bimbiya.server.dto.filter.IngredientSearchDTO;
import com.bimbiya.server.validators.DeleteValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@NoArgsConstructor
@Data
public class IngredientsRequestDTO {
    @NotNull(message = "Ingredients Id required", groups = {  DeleteValidation.class, UpdateValidation.class })
    private Long ingredientsId;

    @NotBlank(message = "Ingredients name required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String ingredientsName;

    @NotBlank(message = "Status required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;

    private String createdUser;

    private String lastUpdatedUser;

    private String activeUser;

    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

    private IngredientSearchDTO ingredientSearchDTO;

}