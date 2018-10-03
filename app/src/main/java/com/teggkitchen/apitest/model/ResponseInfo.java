package com.teggkitchen.apitest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseInfo {
    @SerializedName("code")
    private int code;
    @SerializedName("data")
    public Object data;
    @SerializedName("message")
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
