package com.teggkitchen.apitest;


public interface ItemListClickListener {
    String ELEMENT_ITEM = "ELEMENT_ITEM";
    String ELEMENT_ADD = "ELEMENT_ADD";
    String ELEMENT_REMOVE = "ELEMENT_REMOVE";

    void onItemClicked(int index, String element);
}
