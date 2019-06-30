package com.example.voyage.data.models;

import java.io.Serializable;

public class Seat implements Serializable {

    public static final String SEAT_SEAT_IDS_INTENT_EXTRA = "PICKED_SEATS";

    private final int id;

    private final int available;

    private boolean isAvailable;

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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setSeatAvailable(boolean b) {
        isAvailable = b;
    }
}
