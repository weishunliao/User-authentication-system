package com.develop.web_server.security;

import com.develop.web_server.SpringApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

public class SecurityConstants {


    public static final long EXPERATION_TIME = 864000000; //10day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String EMAIL_VERIFICATION = "/users/verification-email";
    public static final String TOKEN_SECRET = getTokenSecret();

    private static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.getTokenSecret();
    }
}
