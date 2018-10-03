package com.teggkitchen.apitest.model;

public class Member {
    private int id;
    private String email;
    private String password;
    private String token;
    private String phone;
    private String img;
    private int is_email_verify;
    private int is_phone_verify;
    private String created_at;
    private String updated_at;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIs_email_verify() {
        return is_email_verify;
    }

    public void setIs_email_verify(int is_email_verify) {
        this.is_email_verify = is_email_verify;
    }

    public int getIs_phone_verify() {
        return is_phone_verify;
    }

    public void setIs_phone_verify(int is_phone_verify) {
        this.is_phone_verify = is_phone_verify;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
