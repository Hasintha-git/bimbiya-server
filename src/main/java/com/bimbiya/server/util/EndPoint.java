package com.bimbiya.server.util;

public class EndPoint {
    //    --------------------- User ---------------------
    public static final String USER_REQUEST_FILTER_LIST = "/v1/admin-user/filter-list";
    public static final String USER_REQUEST_FIND_ID = "/v1/admin-user/find-id";
    public static final String USER_REQUEST_DETAILS_FOR_DASHBOARD = "/v1/admin-user/for-dashboard";
    public static final String USER_REQUEST_MGT = "/v1/admin-user";
    public static final String USER_REQUEST_FORGET_PASSWORD = "/v1/admin-user/forget-password";
    public static final String LOCK_USER = "/v1/admin-user/lock";
    public static final String UNLOCK_USER = "/v1/admin-user/unlock";
    public static final String USER_REQUEST_SEARCH_DATA = "/v1/admin-user/search-reference-data";

    //    --------------------- Ingredient ---------------------
    public static final String INGREDIENT_REQUEST_FILTER_LIST = "/v1/admin-ingredient/filter-list";
    public static final String INGREDIENT_REQUEST_FIND_ID = "/v1/admin-ingredient/find-id";
    public static final String INGREDIENT_REQUEST_MGT = "/v1/admin-ingredient";
    public static final String INGREDIENT_REQUEST_SEARCH_DATA = "/v1/admin-ingredient/search-reference-data";


    //    --------------------- Ingredient ---------------------
    public static final String BYTE_PACKAGE_REQUEST_FILTER_LIST = "/v1/filter-list";
    public static final String BYTE_PACKAGE_REQUEST_TRENDING_LIST = "/v1/trending-list";
    public static final String BYTE_PACKAGE_CLIENT_REQUEST_FILTER_LIST = "/v1/client/filter-list";
    public static final String BYTE_PACKAGE_REQUEST_FIND_ID = "/v1/find-id";
    public static final String BYTE_PACKAGE_REQUEST_MGT = "/v1/manage";
    public static final String BYTE_PACKAGE_REQUEST_SEARCH_DATA = "/v1/search-reference-data";

    //    --------------------- ORDER ---------------------
    public static final String ADD_TO_CART = "/v1/client-cart/add-to-cart";
    public static final String GET_TO_CART = "/v1/client-cart/get-to-cart";
    public static final String REMOVE_TO_CART = "/v1/client-cart/remove-to-cart";
    public static final String ORDER_REQUEST_SEARCH_DATA = "/v1/admin-order/search-reference-data";
    public static final String ORDER_REQUEST_FILTER_LIST = "/v1/admin-order/filter-list";
    public static final String ORDER_REQUEST_FIND= "/v1/admin-order/find-id";
    public static final String PLACE_ORDER= "/v1/admin-order/place-order";
    public static final String ORDER_REQUEST_UPDATE= "/v1/admin-order/update-status";

}
