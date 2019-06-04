package com.example.voyage.models;

public class User {
    private int id;
    private String emailaddress, name;

    public User(int id, String emailaddress, String name) {
        this.id = id;
        this.emailaddress = emailaddress;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public String getName() {
        return name;
    }
}
