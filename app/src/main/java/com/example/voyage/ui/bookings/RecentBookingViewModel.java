//package com.example.voyage.ui.bookings;
//
//import android.app.Application;
//import android.arch.lifecycle.AndroidViewModel;
//import android.arch.lifecycle.LiveData;
//import android.support.annotation.NonNull;
//
//import com.example.voyage.data.models.Booking;
//import com.example.voyage.data.repositories.VoyageRepository;
//
//import java.util.List;
//
//public class RecentBookingViewModel extends AndroidViewModel {
//    private VoyageRepository voyageRepository = new VoyageRepository();
//
//    public RecentBookingViewModel(@NonNull Application application) {
//        super(application);
//    }
//
//    LiveData<List<Booking>> getBookings() {
//        return voyageRepository.getBookings();
//    }
//}
