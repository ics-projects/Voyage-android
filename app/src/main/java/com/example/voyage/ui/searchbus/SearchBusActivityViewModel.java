package com.example.voyage.ui.searchbus;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

public class SearchBusActivityViewModel extends ViewModel {

    private VoyageRepository voyageRepository;

    public SearchBusActivityViewModel() {
        this.voyageRepository = VoyageRepository.getInstance();
    }

    LiveData<List<Schedule>> getSchedules() {
        return voyageRepository.getSchedules();
    }
}
