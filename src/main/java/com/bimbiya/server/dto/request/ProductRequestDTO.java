package com.bimbiya.server.dto.request;

import com.bimbiya.server.dto.filter.BytePackageSearchDTO;
import com.bimbiya.server.validators.DeleteValidation;
import com.bimbiya.server.validators.InsertValidation;
import com.bimbiya.server.validators.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class ProductRequestDTO {
    @NotNull(message = "Package id required", groups = {  DeleteValidation.class, UpdateValidation.class })
    private Long packageId;

    @NotBlank(message = "Meal name required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String mealName;

    @NotBlank(message = "Description required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String description;

    @NotNull(message = "Price required", groups = {  InsertValidation.class, UpdateValidation.class })
    private BigDecimal price;

    @NotBlank(message = "Portion required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String portion;

    @NotBlank(message = "Status required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String status;

    @NotBlank(message = "Category required", groups = {  InsertValidation.class })
    private String productCategory;

    @NotBlank(message = "Created user required")
    private String createdUser;

    @NotBlank(message = "Last updated user required")
    private String lastUpdatedUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Colombo")
    private Date lastUpdatedTime;


    @NotBlank(message = "Active Username required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String activeUser;

    private int pageNumber;
    private int pageSize;
    private String sortColumn;
    private String sortDirection;

    private BytePackageSearchDTO bytePackageSearchDTO;

    private List<Long> ingredientList;


    @NotBlank(message = "Description required", groups = {  InsertValidation.class, UpdateValidation.class })
    private String img;

    private boolean isWeb;
}