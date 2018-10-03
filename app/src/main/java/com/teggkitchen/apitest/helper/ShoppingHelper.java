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
import com.teggkitchen.apitest.util.Util;

import java.util.ArrayList;
import java.util.List;

public class ShoppingHelper {


    /**
     * 新增購物車
     */
    public static void addShoppingCartItem(Context context, Products product, int num) {

        if (num < 1) return;

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

        // 判斷是否為重複的商品
        boolean needAdd = true;


        for (int i = 0; i < productsTemp.size(); i++) {
            if (productsTemp.get(i).getName().equals(product.getName())) {
                needAdd = false;
                break;
            }
        }

        if (needAdd) {
            //無重複，新增
            productsTemp.add(product);
            numsTemp.add(num);
            shoppingCartTemp.setProducts(productsTemp);
            shoppingCartTemp.setNums(numsTemp);

            // 儲存購物車json到SP
            String products_json = gson.toJson(shoppingCartTemp);
            Util.setSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP, products_json);

            // （檢查）顯示新增後的購物車json
            String jsonn = Util.getSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP);
        }
    }


    /**
     * 加入購物車前，檢查是否重複
     */
    public static boolean isShoppingCartItemRepeat(Context context, Products product) {


        // 判斷是否為重複的商品
        boolean isRepeat = false;

        ShoppingCartTemp shoppingCartTemp = new ShoppingCartTemp();

        // 取得SP的購物車json
        String json = Util.getSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Products> productsTemp;

        if (!Util.isEmpty(json)) {
            shoppingCartTemp = gson.fromJson(json, ShoppingCartTemp.class);
            productsTemp = gson.fromJson(gson.toJson(shoppingCartTemp.getProducts()), new TypeToken<List<Products>>() {
            }.getType());

        } else {
            productsTemp = new ArrayList<>();
        }


        for (int i = 0; i < productsTemp.size(); i++) {
            if (productsTemp.get(i).getName().equals(product.getName())) {
                isRepeat = true;
                break;
            }
        }


        // true -> 重複
        return isRepeat;
    }


    /**
     * 查詢購物車總筆數
     */
    public static int getShoppingCartCount(Context context) {
        ShoppingCartTemp shoppingCartTemp = new ShoppingCartTemp();

        // 取得SP的購物車json
        String json = Util.getSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Products> productsTemp;

        if (!Util.isEmpty(json)) {
            shoppingCartTemp = gson.fromJson(json, ShoppingCartTemp.class);
            productsTemp = gson.fromJson(gson.toJson(shoppingCartTemp.getProducts()), new TypeToken<List<Products>>() {
            }.getType());

        } else {
            productsTemp = new ArrayList<>();

        }
        return productsTemp.size();

    }


    /**
     * 查詢購物車全部資料
     */
    public static ShoppingCartTemp getShoppingCartData(Context context) {
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

        shoppingCartTemp.setProducts(productsTemp);
        shoppingCartTemp.setNums(numsTemp);


        return shoppingCartTemp;

    }


    /**
     * 查詢購物車全部Products
     */
    public static List<Products> getShoppingCartProducts(Context context) {
        ShoppingCartTemp shoppingCartTemp = new ShoppingCartTemp();

        // 取得SP的購物車json
        String json = Util.getSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Products> productsTemp;

        if (!Util.isEmpty(json)) {
            shoppingCartTemp = gson.fromJson(json, ShoppingCartTemp.class);
            productsTemp = gson.fromJson(gson.toJson(shoppingCartTemp.getProducts()), new TypeToken<List<Products>>() {
            }.getType());

        } else {
            productsTemp = new ArrayList<>();

        }
        return productsTemp;

    }


    /**
     * 查詢購物車全部Nums
     */
    public static List<Integer> getShoppingCartNums(Context context) {
        ShoppingCartTemp shoppingCartTemp = new ShoppingCartTemp();

        // 取得SP的購物車json
        String json = Util.getSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP);

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        List<Integer> numsTemp;

        if (!Util.isEmpty(json)) {
            shoppingCartTemp = gson.fromJson(json, ShoppingCartTemp.class);
            numsTemp = gson.fromJson(gson.toJson(shoppingCartTemp.getNums()), new TypeToken<List<Integer>>() {
            }.getType());

        } else {
            numsTemp = new ArrayList<>();

        }
        return numsTemp;

    }


    /**
     * 查詢購物車總金額
     */
    public static int getShoppingCartPriceCount(Context context) {
        int priceCount = 0;
        int priceTemp = 0;
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


        for (int i = 0; i < productsTemp.size(); i++) {
            priceTemp = productsTemp.get(i).getPrice();
            priceCount += (priceTemp * numsTemp.get(i));
        }
        return priceCount;
    }


    /**
     * 刪除購物車單筆
     */
    public static void deleteShoppingCartItem(Context context, Products product) {
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

        // 找尋商品
        boolean needDelete = false;
        for (int i = 0; i < productsTemp.size(); i++) {
            if (productsTemp.get(i).getId() == product.getId() &&
                    productsTemp.get(i).getName().equals(product.getName()) &&
                    productsTemp.get(i).getPrice() == product.getPrice()) {

                productsTemp.remove(i);
                numsTemp.remove(i);
                needDelete = true;
                break;
            }
        }

        if (needDelete) {
            shoppingCartTemp.setProducts(productsTemp);
            shoppingCartTemp.setNums(numsTemp);

            // 儲存購物車json到SP
            String products_json = gson.toJson(shoppingCartTemp);
            Util.setSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP, products_json);
        }
    }


    /**
     * 清除購物車
     */
    public static void clearShoppingCartItem(Context context) {
        Util.setSpData(context, Config.SP_KEY_SHOPPING_CART_TEMP, "");
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
