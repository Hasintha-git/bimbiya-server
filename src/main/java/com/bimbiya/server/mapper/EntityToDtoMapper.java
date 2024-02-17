package com.bimbiya.server.mapper;


import com.bimbiya.server.dto.SimpleBaseDTO;
import com.bimbiya.server.dto.response.*;
import com.bimbiya.server.entity.*;
import com.bimbiya.server.util.enums.ClientDistrictEnum;
import com.bimbiya.server.util.enums.ClientPotionEnum;
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
        if (Objects.nonNull(systemUser.getDistrict())) {
            userResponseDTO.setDistrict(String.valueOf(systemUser.getDistrict()));
            userResponseDTO.setDistrictDescription(ClientDistrictEnum.getEnum(String.valueOf(systemUser.getDistrict())).getDescription());
        }
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

    public static ProductResponseDTO mapBytePackage(Product product, boolean isWeb) {
        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
        productResponseDTO.setPackageId(product.getPackageId());
        productResponseDTO.setMealName(product.getProductName());
        productResponseDTO.setDescription(product.getDescription());
        productResponseDTO.setPrice(product.getPrice());
        productResponseDTO.setProductCategory(product.getProductCategory().getCode());
        productResponseDTO.setStatus(ClientStatusEnum.getEnum(String.valueOf(product.getStatus())).getCode());
        productResponseDTO.setStatusDescription(ClientStatusEnum.getEnum(String.valueOf(product.getStatus())).getDescription());
        if (Objects.nonNull(product.getPotion())) {
            productResponseDTO.setPortion(product.getPotion());
            productResponseDTO.setPortionDescription(ClientPotionEnum.getEnum(product.getPotion()).getDescription());
        }


        if (Objects.nonNull(product.getCreatedUser())) {
            productResponseDTO.setCreatedUser(product.getCreatedUser());
        }

        if (Objects.nonNull(product.getCreatedTime())) {
            productResponseDTO.setCreatedTime(product.getCreatedTime());
        }

        if (Objects.nonNull(product.getLastUpdatedUser())) {
            productResponseDTO.setLastUpdatedUser(product.getLastUpdatedUser());
        }

        if (Objects.nonNull(product.getLastUpdatedTime())) {
            productResponseDTO.setLastUpdatedTime(product.getLastUpdatedTime());
        }

        if (isWeb) {
//            setProductImage(product, productResponseDTO);
            productResponseDTO.setImg(product.getImage());
        }
        return productResponseDTO;
    }

    public static AddToCartResponseDTO mapAddToCart( AddToCart addToCart) {
        AddToCartResponseDTO addToCartResponseDTO = new AddToCartResponseDTO();
        addToCartResponseDTO.setCartId(addToCart.getCartId());
        addToCartResponseDTO.setQty(addToCart.getQty());
        addToCartResponseDTO.setPackageId(addToCart.getBpackage().getPackageId());
        addToCartResponseDTO.setUserId(addToCart.getSystemUser().getId());
        addToCartResponseDTO.setUserName(addToCart.getSystemUser().getUsername());
        addToCartResponseDTO.setMealName(addToCart.getBpackage().getProductName());
        addToCartResponseDTO.setStatus(String.valueOf(addToCart.getStatus()));
        return addToCartResponseDTO;
    }

    public static AddToCartResponseDTO maCartCheckout(AddToCart addToCart, boolean checkout) {
        AddToCartResponseDTO addToCartResponseDTO = new AddToCartResponseDTO();
        addToCartResponseDTO.setCartId(addToCart.getCartId());
        addToCartResponseDTO.setQty(addToCart.getQty());
        addToCartResponseDTO.setPackageId(addToCart.getBpackage().getPackageId());
        addToCartResponseDTO.setMealName(addToCart.getBpackage().getProductName());
        addToCartResponseDTO.setPrice(addToCart.getPrice());
        addToCartResponseDTO.setPersonCount(addToCart.getPersonCount());
        addToCartResponseDTO.setStatus(String.valueOf(addToCart.getStatus()));

        if (checkout) {
            addToCartResponseDTO.setImage(addToCart.getBpackage().getImage());
        }
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


    public static OrderDetailsResponseDTO mapOrderDetails(OrderDetail orderDetail) {
        OrderDetailsResponseDTO responseDTO = new OrderDetailsResponseDTO();
        responseDTO.setOrderDetailId(orderDetail.getDetail_id());
        responseDTO.setProductId(orderDetail.getProduct().getPackageId());
        responseDTO.setProductName(orderDetail.getProduct().getProductName());
        responseDTO.setUnitPrice(orderDetail.getUnitPrice());

        return responseDTO;
    }


    public static SimpleBaseDTO mapProductCategoryDropdown(SimpleBaseDTO simpleBaseDTO, ProductCategory productCategory) {
        simpleBaseDTO.setCode(productCategory.getCode());
        simpleBaseDTO.setDescription(productCategory.getDescription());
        return simpleBaseDTO;
    }
}
