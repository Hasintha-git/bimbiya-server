package com.bimbiya.server.mapper;


import com.bimbiya.server.dto.request.AddToCartRequestDTO;
import com.bimbiya.server.dto.request.BytePackageRequestDTO;
import com.bimbiya.server.dto.request.IngredientsRequestDTO;
import com.bimbiya.server.dto.request.UserRequestDTO;
import com.bimbiya.server.entity.AddToCart;
import com.bimbiya.server.entity.BytePackage;
import com.bimbiya.server.entity.Ingredients;
import com.bimbiya.server.entity.SystemUser;
import com.bimbiya.server.util.enums.Status;

import java.util.Base64;
import java.util.Objects;

public class DtoToEntityMapper {
    private DtoToEntityMapper() {

    }
    public static void mapUser(SystemUser systemUser, UserRequestDTO userRequestDTO) {
        systemUser.setStatus(Status.valueOf(userRequestDTO.getStatus()));
        systemUser.setPwStatus(Status.valueOf(userRequestDTO.getPwStatus()));
        if (Objects.nonNull(userRequestDTO.getPasswordExpireDate())) {
            systemUser.setPasswordExpireDate(userRequestDTO.getPasswordExpireDate());
        }
        systemUser.setLastUpdatedUser(userRequestDTO.getLastUpdatedUser());
        systemUser.setAttempt(0);
        systemUser.setEmail(userRequestDTO.getEmail());
        systemUser.setMobileNo(userRequestDTO.getMobileNo());
        systemUser.setFullName(userRequestDTO.getFullName());
        systemUser.setAddress(userRequestDTO.getAddress());
        systemUser.setCity(userRequestDTO.getCity());
    }

    public static void mapIngredient(Ingredients ingredients, IngredientsRequestDTO ingredientsRequestDTO, boolean isAdd) {
        ingredients.setStatusCode(Status.valueOf(ingredientsRequestDTO.getStatus()));
        ingredients.setIngredientsName(ingredientsRequestDTO.getIngredientsName());
        if (isAdd) {
            ingredients.setCreatedUser(ingredientsRequestDTO.getActiveUserName());
            ingredients.setLastUpdatedUser(ingredientsRequestDTO.getActiveUserName());

        }else {
            ingredients.setLastUpdatedUser(ingredientsRequestDTO.getActiveUserName());
        }
    }

    public static void mapBytePackage(BytePackage bytePackage, BytePackageRequestDTO bytePackageRequestDTO, boolean isAdd) {
        System.out.println(">>"+bytePackageRequestDTO.getMealName());
        bytePackage.setMealName(bytePackageRequestDTO.getMealName());
        System.out.println(">>"+bytePackage.getMealName());
        bytePackage.setDescription(bytePackageRequestDTO.getDescription());
        bytePackage.setPrice(bytePackageRequestDTO.getPrice());
        bytePackage.setPotion(bytePackageRequestDTO.getPortion());
        bytePackage.setStatus(bytePackageRequestDTO.getStatus());
        if (isAdd) {
            bytePackage.setCreatedUser(bytePackageRequestDTO.getActiveUser());
            bytePackage.setLastUpdatedUser(bytePackageRequestDTO.getActiveUser());

        }else {
            bytePackage.setLastUpdatedUser(bytePackageRequestDTO.getActiveUser());
        }

        if (Objects.nonNull(bytePackageRequestDTO.getImg())) {
            String[] fileName = bytePackageRequestDTO.getImg().split(",", 2);
            byte[] decode = Base64.getDecoder().decode(fileName[1]);

            bytePackage.setImg(decode);
        }
    }

    public static void mapAddToCart(AddToCart addToCart, AddToCartRequestDTO addToCartRequestDTO) {
        addToCart.setQty(addToCartRequestDTO.getQty());
        addToCart.setStatus(Status.valueOf(addToCartRequestDTO.getStatus()));

    }
}
