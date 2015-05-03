package com.theunitedtraders.javatest.domain.json.response.base;

/**
 * Simple message container. Being converted into JSON representation on response.
 */
public class SimpleMessageResponse {

    private String message;

    public SimpleMessageResponse() {
    }

    public SimpleMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
