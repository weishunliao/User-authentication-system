package com.develop.web_server.exception;

public class UserServiceException extends RuntimeException{
    private static final long serialVersionUID = 6924201697429724836L;

    public UserServiceException(String message) {
        super(message);
    }
}
