package com.example.voyage.data.models;

import com.google.gson.annotations.SerializedName;

public class SeatPrice {

    @SerializedName("First class")
    private final int firstClassSeatPrice;

    @SerializedName("Second class")
    private final int secondClassSeatPrice;

    public SeatPrice(int firstClassSeatPrice, int secondClassSeatPrice) {
        this.firstClassSeatPrice = firstClassSeatPrice;
        this.secondClassSeatPrice = secondClassSeatPrice;
    }

    public int getFirstClassSeatPrice() {
        return firstClassSeatPrice;
    }

    public int getSecondClassSeatPrice() {
        return secondClassSeatPrice;
    }
}
