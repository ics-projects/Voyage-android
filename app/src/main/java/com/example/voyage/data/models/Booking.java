package com.example.voyage.data.models;

public class Booking {

    private String destination;
    private String origin;
    private String dateBooked;
    private String amount;
    private int confirmed;

    public Booking(String destination, String origin, String dateBooked, String amount, int confirmed) {
        this.destination = destination;
        this.origin = origin;
        this.dateBooked = dateBooked;
        this.amount = amount;
        this.confirmed = confirmed;
    }

    public String getDestination() {
        return destination;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDateBooked() {
        return dateBooked;
    }

    public String getAmount() {
        return amount;
    }

    public int getConfirmed() {
        return confirmed;
    }
}
