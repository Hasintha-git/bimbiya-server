package com.bimbiya.server.util;

public class EndPoint {
    //    --------------------- User ---------------------
    public static final String USER_REQUEST_FILTER_LIST = "/v1/admin-user/filter-list";
    public static final String USER_REQUEST_FIND_ID = "/v1/admin-user/find-id";
    public static final String USER_REQUEST_MGT = "/v1/admin-user";
    public static final String USER_REQUEST_SEARCH_DATA = "/v1/admin-user/search-reference-data";

    //    --------------------- Ingredient ---------------------
    public static final String INGREDIENT_REQUEST_FILTER_LIST = "/v1/admin-ingredient/filter-list";
    public static final String INGREDIENT_REQUEST_FIND_ID = "/v1/admin-ingredient/find-id";
    public static final String INGREDIENT_REQUEST_MGT = "/v1/admin-ingredient";
    public static final String INGREDIENT_REQUEST_SEARCH_DATA = "/v1/admin-ingredient/search-reference-data";


    //    --------------------- Ingredient ---------------------
    public static final String BYTE_PACKAGE_REQUEST_FILTER_LIST = "/v1/admin-byte/filter-list";
    public static final String BYTE_PACKAGE_REQUEST_FIND_ID = "/v1/admin-byte/find-id";
    public static final String BYTE_PACKAGE_REQUEST_MGT = "/v1/admin-byte";
    public static final String BYTE_PACKAGE_REQUEST_SEARCH_DATA = "/v1/admin-byte/search-reference-data";

    //    --------------------- ORDER ---------------------
    public static final String ADD_TO_CART = "/v1/client-cart/add-to-cart";
    public static final String GET_TO_CART = "/v1/client-cart/get-to-cart";
    public static final String REMOVE_TO_CART = "/v1/client-cart/remove-to-cart";
}
