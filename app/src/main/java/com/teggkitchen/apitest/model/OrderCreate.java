package com.teggkitchen.apitest.model;

import java.util.List;

public class OrderCreate {
    private String token;
    private List<OrderDetails> order_details;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public List<OrderDetails> getOrderDetails() {
        return order_details;
    }

    public void setOrderDetails(List<OrderDetails> orderDetails) {
        this.order_details = orderDetails;
    }
}
