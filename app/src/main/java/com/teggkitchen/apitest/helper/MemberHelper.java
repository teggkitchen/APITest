package com.teggkitchen.apitest.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.teggkitchen.apitest.config.Config;
import com.teggkitchen.apitest.model.OrderCreate;
import com.teggkitchen.apitest.model.OrderDetails;
import com.teggkitchen.apitest.model.Products;
import com.teggkitchen.apitest.model.ShoppingCartTemp;
import com.teggkitchen.apitest.page.member.MemberSignUpActivity;
import com.teggkitchen.apitest.util.Util;

import java.util.ArrayList;
import java.util.List;

public class MemberHelper {


    /**
     * 設定Token
     */
    public static void setToken(Context context,String token){
        Util.setSpData(context, Config.SP_KEY_MEMBER_TOKEN, token);
    }

    /**
     * 取得Token
     */
    public static String getToken(Context context){
        return Util.getSpData(context, Config.SP_KEY_MEMBER_TOKEN);
    }

    /**
     * 清空Token
     */
    public static void clearToken(Context context){
        Util.setSpData(context, Config.SP_KEY_MEMBER_TOKEN,null);
    }

    /**
     * 設定Member
     */
    public static void setMember(Context context,String token){
        Util.setSpData(context, Config.SP_KEY_MEMBER,token);
    }

    /**
     * 取得Member
     */
    public static String getMember(Context context){
        return Util.getSpData(context, Config.SP_KEY_MEMBER);
    }

    /**
     * 清空Member
     */
    public static void clearMember(Context context){
        Util.setSpData(context, Config.SP_KEY_MEMBER,null);
    }

    /**
     * 新增購物車
     */
    public static OrderCreate convertShoppingCartToOrder(Context context,String token) {
        ShoppingCartTemp shoppingCartTemp = new ShoppingCartTemp();

        // 取得SP的購物車json
        String json = Util.getSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Products> productsTemp;
        List<Integer> numsTemp;

        if (!Util.isEmpty(json)) {
            shoppingCartTemp = gson.fromJson(json, ShoppingCartTemp.class);
            productsTemp = gson.fromJson(gson.toJson(shoppingCartTemp.getProducts()), new TypeToken<List<Products>>() {
            }.getType());
            numsTemp = gson.fromJson(gson.toJson(shoppingCartTemp.getNums()), new TypeToken<List<Integer>>() {
            }.getType());

        } else {
            productsTemp = new ArrayList<>();
            numsTemp = new ArrayList<>();

        }

        // 購物車資料轉為OrderCreate
        OrderCreate orderCreate = new OrderCreate();
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        orderCreate.setToken(token);

        for (int i = 0; i < productsTemp.size(); i++) {
            OrderDetails orderDetails =new OrderDetails();
            orderDetails.setNum(numsTemp.get(i));
            orderDetails.setProduct_id(productsTemp.get(i).getId());
            orderDetailsList.add(orderDetails);
        }
        orderCreate.setOrderDetails(orderDetailsList);

        if (Util.isEmpty(orderCreate.getOrderDetails())){
            Log.e("getOrderDetails-1","null");
            return null;
        }
        Log.e("getOrderDetails-2",gson.toJson(orderCreate));

        return orderCreate;

    }
}
