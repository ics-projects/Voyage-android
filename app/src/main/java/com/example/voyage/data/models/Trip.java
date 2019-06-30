package com.example.voyage.data.models;

import com.google.gson.annotations.SerializedName;

public class Trip {

    public static final String TRIP_PICK_POINT_INTENT_EXTRA = "TRIP_PICK_POINT";
    public static final String TRIP_ID_INTENT_EXTRA = "TRIP_ID";
    public static final String TRIP_DROP_POINT_INTENT_EXTRA = "TRIP_DROP_POINT";
    public static final String TRIP_BUS_ID_INTENT_EXTRA = "TRIP_BUS_ID";

    private final int id;
    private final String origin;
    private final String destination;
    private final int busId;

    @SerializedName("dept_time")
    private final String departureTime;

    @SerializedName("arrival_time")
    private final String arrivalTime;

    @SerializedName("prices")
    private SeatPrice seatPrice;

    @SerializedName("origins")
    private final Stage originStage;

    @SerializedName("destinations")
    private final Stage destinationStage;

    public Trip(int id, String origin, String destination, int busId, String departureTime, String arrivalTime,
                SeatPrice seatPrice, Stage originStage, Stage destinationStage) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.busId = busId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.seatPrice = seatPrice;
        this.originStage = originStage;
        this.destinationStage = destinationStage;
    }

    public int getId() {
        return id;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getBusId() {
        return busId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    private SeatPrice getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(SeatPrice seatPrice) {
        this.seatPrice = seatPrice;
    }

    public int getFirstClassPrice() {
        return getSeatPrice().getFirstClassSeatPrice();
    }

    public int getSecondClassPrice() {
        return getSeatPrice().getSecondClassSeatPrice();
    }

    public Stage getOriginStage() {
        return originStage;
    }

    public Stage getDestinationStage() {
        return destinationStage;
    }
}
