package com.example.voyage.data.models;

public class Schedule {

    private final String origin;
    private final String destination;

    public Schedule(String origin, String destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }
}
