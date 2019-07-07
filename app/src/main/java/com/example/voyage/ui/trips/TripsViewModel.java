package com.example.voyage.ui.trips;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.voyage.data.models.Trip;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

public class TripsViewModel extends ViewModel {

    private VoyageRepository voyageRepository;

    public TripsViewModel() {
        this.voyageRepository = VoyageRepository.getInstance();
    }

    LiveData<List<Trip>> getTrips(String origin, String destination, String date) {
        return voyageRepository.getTrips(origin, destination, date);
    }
}
