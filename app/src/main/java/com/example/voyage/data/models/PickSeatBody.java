package com.example.voyage.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PickSeatBody {

    @SerializedName("pick-point")
    @Expose
    private int pickPoint;

    @SerializedName("drop-point")
    @Expose
    private int dropPoint;

    @SerializedName("trip_id")
    @Expose
    private int tripId;

    @SerializedName("seats")
    @Expose
    private ArrayList<Integer> seats;

    public PickSeatBody(int pickPoint, int dropPoint, int tripId, ArrayList<Integer> seats) {
        this.pickPoint = pickPoint;
        this.dropPoint = dropPoint;
        this.tripId = tripId;
        this.seats = seats;
    }

    public int getPickPoint() {
        return pickPoint;
    }

    public void setPickPoint(int pickPoint) {
        this.pickPoint = pickPoint;
    }

    public int getDropPoint() {
        return dropPoint;
    }

    public void setDropPoint(int dropPoint) {
        this.dropPoint = dropPoint;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public ArrayList<Integer> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<Integer> seats) {
        this.seats = seats;
    }
}
