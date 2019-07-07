package com.example.voyage.ui.bookings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.voyage.data.models.Booking;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

public class RecentBookingViewModel extends ViewModel {
    private VoyageRepository voyageRepository;

    public RecentBookingViewModel() {
        voyageRepository = VoyageRepository.getInstance();
    }

    LiveData<List<Booking>> getBookings() {
        return voyageRepository.getBookings();
    }
}
