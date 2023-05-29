package com.resh.coders.springmvc.models;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder
public class restResponse {
    private String message;
    private int statusCode;
    private Object response;

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public restResponse() {
        setMessage("failed");
		setStatusCode(500);
		setResponse(null);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "{" +
                "message='" + message + '\'' +
                ", statusCode=" + statusCode +
                ", response=" + response +
                '}';
    }
}
