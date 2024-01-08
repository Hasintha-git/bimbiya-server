package com.bimbiya.server.mapper;


import com.bimbiya.server.dto.request.*;
import com.bimbiya.server.entity.*;
import com.bimbiya.server.util.enums.Status;

import java.util.Base64;
import java.util.Date;
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
            ingredients.setCreatedUser(ingredientsRequestDTO.getActiveUser());
            ingredients.setCreatedTime(new Date());

        }
        ingredients.setLastUpdatedUser(ingredientsRequestDTO.getActiveUser());
    }

    public static void mapBytePackage(Product product, ProductRequestDTO productRequestDTO, boolean isAdd) {
        System.out.println(">>"+ productRequestDTO.getMealName());
        product.setProductName(productRequestDTO.getMealName());
        System.out.println(">>"+ product.getProductName());
        product.setDescription(productRequestDTO.getDescription());
        product.setPrice(productRequestDTO.getPrice());

        if (Objects.nonNull(productRequestDTO.getPortion())) {
            product.setPotion(productRequestDTO.getPortion());
        }
        product.setStatus(Status.valueOf(productRequestDTO.getStatus()));
        if (isAdd) {
            product.setCreatedUser(productRequestDTO.getActiveUser());
            product.setLastUpdatedUser(productRequestDTO.getActiveUser());

        }else {
            product.setLastUpdatedUser(productRequestDTO.getActiveUser());
        }

        if (Objects.nonNull(productRequestDTO.getImg())) {
            String[] fileName = productRequestDTO.getImg().split(",", 2);
            byte[] decode = Base64.getDecoder().decode(fileName[1]);

            product.setImg(decode);
        }
    }

    public static void mapAddToCart(AddToCart addToCart, AddToCartRequestDTO addToCartRequestDTO) {
        addToCart.setQty(addToCartRequestDTO.getQty());
        addToCart.setStatus(Status.valueOf(addToCartRequestDTO.getStatus()));

    }

    public static void mapOrder(Order order, OrderRequestDTO orderRequestDTO) {
        Date date = new Date();
        order.setOrderDate(date);
        order.setTotalAmount(orderRequestDTO.getTotAmount());
        order.setCreatedUser(orderRequestDTO.getActiveUser());
        order.setLastUpdatedUser(orderRequestDTO.getActiveUser());
        order.setCreatedTime(date);
        order.setLastUpdatedTime(date);
    }

    public static void mapOrderDetails(OrderDetail orderDetail, OrderRequestDTO orderRequestDTO) {
        orderDetail.setQuantity(orderRequestDTO.getQty());
        orderDetail.setUnitPrice(orderRequestDTO.getTotAmount());
        orderDetail.setSubTotal(orderRequestDTO.getSubTotal());
        if (Objects.nonNull(orderRequestDTO.getPotion())) {
            orderDetail.setPotion(orderRequestDTO.getPotion());
        }
    }
}
