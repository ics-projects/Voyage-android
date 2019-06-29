package com.example.voyage.ui.trips;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.voyage.data.models.Trip;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

public class TripsViewModel extends AndroidViewModel {

    private VoyageRepository voyageRepository;

    public TripsViewModel(@NonNull Application application) {
        super(application);
        this.voyageRepository = new VoyageRepository();
    }

    LiveData<List<Trip>> getTrips(String origin, String destination, String date) {
        return voyageRepository.getTrips(origin, destination, date);
    }
}
