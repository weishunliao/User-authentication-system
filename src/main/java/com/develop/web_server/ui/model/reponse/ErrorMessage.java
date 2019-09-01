package com.develop.web_server.ui.model.reponse;

import java.util.Date;

public class ErrorMessage {

    private Date time;
    private String message;

    public ErrorMessage(Date time, String message) {
        this.time = time;
        this.message = message;
    }

    public ErrorMessage() {
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
