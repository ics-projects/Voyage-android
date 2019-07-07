package com.example.voyage.ui.pay;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.voyage.data.repositories.VoyageRepository;

import java.util.ArrayList;

public class PayViewModel extends ViewModel {

    private VoyageRepository voyageRepository;

    public PayViewModel() {
        voyageRepository = VoyageRepository.getInstance();
    }

    LiveData<Integer> pay(String url, String phoneNumber, int tripId, int pickPoint, int dropPoint,
                          ArrayList<Integer> intentSeatIds) {
        return voyageRepository.pay(url, phoneNumber, tripId, pickPoint, dropPoint, intentSeatIds);
    }
}
