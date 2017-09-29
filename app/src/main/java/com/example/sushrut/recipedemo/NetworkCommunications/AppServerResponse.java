package com.example.sushrut.recipedemo.NetworkCommunications;

/**
 * Created by Sushrut on 9/29/2017.
 */

public class AppServerResponse {
    private int httpStatusCode;
    private String message;

    public AppServerResponse() {
        this.httpStatusCode = 200;
        this.message = "Success";
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
