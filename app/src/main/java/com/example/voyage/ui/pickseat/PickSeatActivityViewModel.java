package com.example.voyage.ui.pickseat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.voyage.data.models.Seat;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

public class PickSeatActivityViewModel extends AndroidViewModel {
    private final VoyageRepository voyageRepository;

    public PickSeatActivityViewModel(@NonNull Application application) {
        super(application);
        this.voyageRepository = new VoyageRepository();
    }

    LiveData<List<Seat>> getSeats(int busId) {
        return voyageRepository.getSeats(busId);
    }
}
