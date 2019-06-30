package com.example.voyage.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayDetails {

    @SerializedName("trip_id")
    private int tripId;

    private String time;

    private List<Stage> stages;

    private List<Seat> seats;

    @SerializedName("total_price")
    private int totalPrice;

    @SerializedName("pay_URL")
    private String payUrl;

    public PayDetails(int tripId, String time, List<Stage> stages, List<Seat> seats, int totalPrice, String payUrl) {
        this.tripId = tripId;
        this.time = time;
        this.stages = stages;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.payUrl = payUrl;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getTime() {
        return time;
    }
}
