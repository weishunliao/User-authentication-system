package com.develop.web_server.security;

import com.develop.web_server.SpringApplicationContext;

public class SecurityConstants {


    public static final long EXPIRATION_TIME = 864000000; //10day
    public static final long PASSWORD_RESET_TOKEN_EXPIRATION_TIME = 3600000; //1hr
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String EMAIL_VERIFICATION = "/users/email-verification";
    public static final String TOKEN_SECRET = getTokenSecret();
    public static final String PASSWORD_REST = "/password-reset";
    public static final String PASSWORD_REST_REQUEST = "/users/password-reset-request";
    public static final String SET_NEW_PASSWORD = "/users/new-password";

    private static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
