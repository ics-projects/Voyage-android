package com.example.voyage.ui.searchbus;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

public class SearchBusActivityViewModel extends AndroidViewModel {

    private VoyageRepository voyageRepository;

    public SearchBusActivityViewModel(@NonNull final Application application) {
        super(application);
        this.voyageRepository = new VoyageRepository();
    }

    LiveData<List<Schedule>> getSchedules() {
        return voyageRepository.getSchedules();
    }
}
