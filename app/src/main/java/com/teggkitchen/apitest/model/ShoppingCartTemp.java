package com.teggkitchen.apitest.model;

import java.util.List;

public class ShoppingCartTemp {
    private List<Products> products;
    private List<Integer> nums;

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public List<Integer> getNums() {
        return nums;
    }

    public void setNums(List<Integer> nums) {
        this.nums = nums;
    }
}
