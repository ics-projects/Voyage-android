package com.example.voyage.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PayRequestBody {

    @SerializedName("mobile-no")
    @Expose
    private final String mobileNo;

    @SerializedName("pick-point")
    @Expose
    private final int pickPoint;

    @SerializedName("drop-point")
    @Expose
    private final int dropPoint;

    @SerializedName("trip_id")
    @Expose
    private final int tripId;

    @SerializedName("seats")
    @Expose
    private final List<Integer> seatIds;

    public PayRequestBody(String mobileNo, int pickPoint, int dropPoint, int tripId,
                          List<Integer> seatIds) {
        this.mobileNo = mobileNo;
        this.pickPoint = pickPoint;
        this.dropPoint = dropPoint;
        this.tripId = tripId;
        this.seatIds = seatIds;
    }
}
