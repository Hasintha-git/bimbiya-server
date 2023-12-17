package com.bimbiya.server.mapper;


import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.response.*;
import com.bimbiya.server.entity.*;
import com.bimbiya.server.util.enums.ClientStatusEnum;

import java.util.Objects;

public class EntityToDtoMapper {
    private EntityToDtoMapper() {

    }
    public static UserResponseDTO mapUser(SystemUser systemUser) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(systemUser.getId());
        userResponseDTO.setUsername(systemUser.getUsername());
        userResponseDTO.setEmail(systemUser.getEmail());
        userResponseDTO.setStatus(String.valueOf(systemUser.getStatus()));
        userResponseDTO.setPwStatus(String.valueOf(systemUser.getPwStatus()));
        userResponseDTO.setCity(systemUser.getCity());
        userResponseDTO.setNic(systemUser.getNic());
        userResponseDTO.setDateOfBirth(systemUser.getDateOfBirth());
        userResponseDTO.setFullName(systemUser.getFullName());
        userResponseDTO.setMobileNo(systemUser.getMobileNo());
        userResponseDTO.setAddress(systemUser.getAddress());
        userResponseDTO.setUserRole(systemUser.getUserRole().getCode());
        userResponseDTO.setUserRoleDescription(systemUser.getUserRole().getDescription());

        if (Objects.nonNull(systemUser.getPasswordExpireDate())) {
            userResponseDTO.setPasswordExpireDate(systemUser.getPasswordExpireDate());
        }

        if (Objects.nonNull(systemUser.getLastLoggedDate())) {
            userResponseDTO.setLastLoggedDate(systemUser.getLastLoggedDate());
        }

        if (Objects.nonNull(systemUser.getCreatedUser())) {
            userResponseDTO.setCreatedUser(systemUser.getCreatedUser());
        }

        if (Objects.nonNull(systemUser.getCreatedTime())) {
            userResponseDTO.setCreatedTime(systemUser.getCreatedTime());
        }

        if (Objects.nonNull(systemUser.getLastUpdatedUser())) {
            userResponseDTO.setLastUpdatedUser(systemUser.getLastUpdatedUser());
        }

        if (Objects.nonNull(systemUser.getLastUpdatedTime())) {
            userResponseDTO.setLastUpdatedTime(systemUser.getLastUpdatedTime());
        }

        return userResponseDTO;
    }

    public static SimpleBaseDTO mapUserRoleDropdown(SimpleBaseDTO simpleBaseDTO, UserRole userRole) {
        simpleBaseDTO.setCode(userRole.getCode());
        simpleBaseDTO.setDescription(userRole.getDescription());
        return simpleBaseDTO;
    }

    public static SimpleBaseDTO mapIngredientDropdown(SimpleBaseDTO simpleBaseDTO, Ingredients ingredients) {
        simpleBaseDTO.setId(ingredients.getIngredientsId());
        simpleBaseDTO.setDescription(ingredients.getIngredientsName());
        return simpleBaseDTO;
    }


    public static IngredientsResponseDTO mapIngredient(Ingredients ingredients) {
        IngredientsResponseDTO ingredientsResponseDTO = new IngredientsResponseDTO();
        ingredientsResponseDTO.setIngredientsId(ingredients.getIngredientsId());
        ingredientsResponseDTO.setIngredientsName(ingredients.getIngredientsName());
        ingredientsResponseDTO.setStatus(String.valueOf(ingredients.getStatusCode()));

        if (Objects.nonNull(ingredients.getCreatedUser())) {
            ingredientsResponseDTO.setCreatedUser(ingredients.getCreatedUser());
        }

        if (Objects.nonNull(ingredients.getCreatedTime())) {
            ingredientsResponseDTO.setCreatedTime(ingredients.getCreatedTime());
        }

        if (Objects.nonNull(ingredients.getLastUpdatedUser())) {
            ingredientsResponseDTO.setLastUpdatedUser(ingredients.getLastUpdatedUser());
        }

        if (Objects.nonNull(ingredients.getLastUpdatedTime())) {
            ingredientsResponseDTO.setLastUpdatedTime(ingredients.getLastUpdatedTime());
        }

        return ingredientsResponseDTO;
    }

    public static BytePackageResponseDTO mapBytePackage(BytePackage bytePackage) {
        BytePackageResponseDTO bytePackageResponseDTO = new BytePackageResponseDTO();
        bytePackageResponseDTO.setPackageId(bytePackage.getPackageId());
        bytePackageResponseDTO.setMealName(bytePackage.getMealName());
        bytePackageResponseDTO.setDescription(bytePackage.getDescription());
        bytePackageResponseDTO.setPrice(bytePackage.getPrice());
        bytePackageResponseDTO.setStatus(ClientStatusEnum.getEnum(String.valueOf(bytePackage.getStatus())).getCode());
        bytePackageResponseDTO.setStatusDescription(ClientStatusEnum.getEnum(String.valueOf(bytePackage.getStatus())).getDescription());
        bytePackageResponseDTO.setPortion(String.valueOf(bytePackage.getPotion()));

        if (Objects.nonNull(bytePackage.getCreatedUser())) {
            bytePackageResponseDTO.setCreatedUser(bytePackage.getCreatedUser());
        }

        if (Objects.nonNull(bytePackage.getCreatedTime())) {
            bytePackageResponseDTO.setCreatedTime(bytePackage.getCreatedTime());
        }

        if (Objects.nonNull(bytePackage.getLastUpdatedUser())) {
            bytePackageResponseDTO.setLastUpdatedUser(bytePackage.getLastUpdatedUser());
        }

        if (Objects.nonNull(bytePackage.getLastUpdatedTime())) {
            bytePackageResponseDTO.setLastUpdatedTime(bytePackage.getLastUpdatedTime());
        }

        return bytePackageResponseDTO;
    }

    public static AddToCartResponseDTO mapAddToCart( AddToCart addToCart) {
        AddToCartResponseDTO addToCartResponseDTO = new AddToCartResponseDTO();
        addToCartResponseDTO.setCartId(addToCart.getCartId());
        addToCartResponseDTO.setQty(addToCart.getQty());
        addToCartResponseDTO.setPackageId(addToCart.getBpackage().getPackageId());
        addToCartResponseDTO.setUserId(addToCart.getSystemUser().getId());
        addToCartResponseDTO.setUserName(addToCart.getSystemUser().getUsername());
        addToCartResponseDTO.setMealName(addToCart.getBpackage().getMealName());
        addToCartResponseDTO.setStatus(String.valueOf(addToCart.getStatus()));
        return addToCartResponseDTO;
    }

    public static OrderResponseDTO mapOrder(Order order) {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(order.getId());
        orderResponseDTO.setUserId(order.getSystemUser().getId());
        orderResponseDTO.setOrderDate(order.getOrderDate());
        orderResponseDTO.setTotalAmount(order.getTotalAmount());
        orderResponseDTO.setStatus(String.valueOf(order.getStatus()));

        if (Objects.nonNull(order.getCreatedUser())) {
            orderResponseDTO.setCreatedUser(order.getCreatedUser());
        }

        if (Objects.nonNull(order.getCreatedTime())) {
            orderResponseDTO.setCreatedTime(order.getCreatedTime());
        }

        if (Objects.nonNull(order.getLastUpdatedUser())) {
            orderResponseDTO.setLastUpdatedUser(order.getLastUpdatedUser());
        }

        if (Objects.nonNull(order.getLastUpdatedTime())) {
            orderResponseDTO.setLastUpdatedTime(order.getLastUpdatedTime());
        }

        return orderResponseDTO;
    }

}
