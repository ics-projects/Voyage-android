package com.example.voyage.data.models;

import com.google.gson.annotations.SerializedName;

public class Schedule {

    private final String origin;
    private final String destination;

    @SerializedName("dept_time")
    private final String date;

    public Schedule(String origin, String destination, String date) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }
}
