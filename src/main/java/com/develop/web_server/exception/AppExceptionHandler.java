package com.develop.web_server.exception;


import com.develop.web_server.ui.model.reponse.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {


    @ExceptionHandler(value = UserServiceException.class)
    public ResponseEntity<Object> handleUserException(UserServiceException exception, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleUserException(Exception exception, WebRequest webRequest) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
