package com.example.voyage.data.models;

public class RecentBookings {

    private final String origin;
    private final String destination;
    private final String seatPrice;
    private final String dateBooked;

    public RecentBookings(String origin, String destination, String dateBooked,
                          String seatPrice) {
        this.origin = origin;
        this.destination = destination;
        this.dateBooked = dateBooked;
        this.seatPrice = seatPrice;
    }
    public String getOrigin()
    {
        return origin;
    }
    public String getDestination() {

        return destination;
    }

    public String getDateBooked() {

        return dateBooked;
    }
    public String getSeatPrice() {

        return seatPrice;
    }

}