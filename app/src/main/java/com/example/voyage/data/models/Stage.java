package com.example.voyage.data.models;

public class Stage {

    private final int id;
    private final String name;

    public Stage(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
