package com.example.voyage.data.models;

import com.google.gson.annotations.SerializedName;

public class Trip {

    private final String origin;
    private final String destination;

    @SerializedName("dept_time")
    private final String departureTime;

    @SerializedName("arrival_time")
    private final String arrivalTime;

    @SerializedName("prices")
    private SeatPrice seatPrice;

    public Trip(String origin, String destination, String departureTime, String arrivalTime,
                SeatPrice seatPrice) {
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatPrice = seatPrice;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public SeatPrice getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(SeatPrice seatPrice) {
        this.seatPrice = seatPrice;
    }

    public int getFirstClassPrice() {
        return getSeatPrice().getFirstClassSeatPrice();
    }
}
