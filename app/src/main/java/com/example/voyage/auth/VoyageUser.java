package com.example.voyage.auth;

import com.google.gson.annotations.SerializedName;

public class VoyageUser {
    private String token;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    private String email;

    private Throwable throwable;

    private VoyageUser(String token, String firstName, String lastName, String email) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public VoyageUser(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
