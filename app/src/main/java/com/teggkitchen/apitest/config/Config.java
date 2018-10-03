package com.teggkitchen.apitest.config;

import okhttp3.MediaType;

public class Config {
    public final static String URL_PRODUCT = "http://54.238.175.181:8000";
    public final static String URL_DEBUG = "http://192.168.43.42:8000";

    public final static String URL = URL_DEBUG;

    public final static String PRODUCT_ADD = URL + "/product";
    public final static String PRODUCT_SHOW = URL + "/products";
    public final static String PRODUCT_UPDATE = URL + "/product"; //product/7
    public final static String PRODUCT_DELETE = URL + "/product"; //product/7

    public final static String MEMBER_SIGNUP = URL + "/member/signup";
    public final static String MEMBER_SIGNIN = URL + "/member/signin";

    public final static String ORDER_ADD = URL + "/order/create";
    public final static String ORDER_SHOW = URL + "/order/query";
    public final static String ORDER_DETAIL_SHOW= URL + "/order/querydetail";
    public final static String ORDER_DELETE = URL + "/order/delete";

    public final static String IMAGE_SHOW = URL + "/image/";

    public final static String SP_SOURCE = "sp_source";
    public final static String SP_KEY_MEMBER_TOKEN = "token";
    public final static String SP_KEY_MEMBER = "member";
    public final static String SP_KEY_PRODUCT_TEMP = "product_temp";
    public final static String SP_KEY_ORDER_TEMP = "order_temp";
    public final static String SP_KEY_SHOPPING_CART_TEMP = "shopping_cart_temp";


    public final static int CODE_SUCCESS = 10;
    public final static int CODE_ERROR = 20;
    public final static String SEND_MENU_STATUS_CHANGE = "SEND_MENU_STATUS_CHANGE";


    public static final MediaType JSON_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    public static final String GOPAGE_KEY_GOBACK = "GOPAGE_KEY_GOBACK";
    public static final String GOPAGE_GOBACK = "GOBACK";

    public static final double RECP_IMAGE_HEIGHT_RATIO_4_3_H = 0.75;    // 4:3 => 3/4 (高度 = 寬度x0.75)
    public static final double RECP_IMAGE_HEIGHT_RATIO_4_3_W = 1.3333;  // 4:3 => 4/3 (高度 = 寬度x1.3333)
    public static final double RECP_IMAGE_HEIGHT_RATIO_16_9_H = 0.5625; // 16:9 => 9/16 (高度 = 寬度x0.5625)
    public static final double RECP_IMAGE_HEIGHT_RATIO_16_9_W = 1.7777; // 16:9 => 16/9 (寬度 = 高度x1.7777)

}
