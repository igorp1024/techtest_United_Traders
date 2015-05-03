package com.theunitedtraders.javatest.domain.json.response;

import com.theunitedtraders.javatest.domain.json.response.base.SimpleMessageResponse;

/**
 * Authorized user response container. Being converted into JSON representation on response.
 */
public class AuthorizedResponse extends SimpleMessageResponse {

    private int count;

    public AuthorizedResponse() {
    }

    public AuthorizedResponse(String message, int count) {
        super(message);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
