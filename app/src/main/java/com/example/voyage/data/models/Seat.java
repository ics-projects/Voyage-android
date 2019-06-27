package com.example.voyage.data.models;

public class Seat {

    private final int id;
    private final int available;

    public Seat(int id, int available) {
        this.id = id;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public int getAvailable() {
        return available;
    }
}
