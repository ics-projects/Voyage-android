package com.example.voyage.ui.pickseat;

import com.example.voyage.data.models.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatRowCollection {

    private List<List<Seat>> rowSeats = new ArrayList<>();

    public void add(List<Seat> seats) {
        rowSeats.add(seats);
    }

    public List<List<Seat>> getRowSeats() {
        return rowSeats;
    }

    public void setRowSeats(List<List<Seat>> rowSeats) {
        this.rowSeats = rowSeats;
    }
}
