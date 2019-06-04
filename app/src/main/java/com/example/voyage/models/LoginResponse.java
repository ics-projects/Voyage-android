package com.example.voyage.models;

public class LoginResponse {
    private String token;
    private String error;

    public LoginResponse(String message) {
        if (message == "UnAuthorised") {
            this.error = message;
        } else {
            this.token = message;
        }
    }
}
