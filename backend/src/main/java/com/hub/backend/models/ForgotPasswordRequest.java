package com.hub.backend.models;

public class ForgotPasswordRequest {

    private String usernameOrEmail;

    public ForgotPasswordRequest() {}

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }
    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}