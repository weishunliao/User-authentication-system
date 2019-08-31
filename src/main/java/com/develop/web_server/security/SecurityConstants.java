package com.develop.web_server.security;

public class SecurityConstants {
    public static final long EXPERATION_TIME = 864000000; //10day
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "2k13mlk123";
}
