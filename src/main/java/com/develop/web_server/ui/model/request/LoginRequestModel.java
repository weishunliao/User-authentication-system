package com.develop.web_server.ui.model.request;

public class LoginRequestModel {


    private String password;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "LoginRequestModel{" +
                "password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
