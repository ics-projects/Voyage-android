package com.example.voyage.ui.pickseat;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.voyage.data.models.PayDetails;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class PickSeatViewModel extends ViewModel {
    private final VoyageRepository voyageRepository;
    private final int busId;

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<SeatRowCollection> seatRowCollectionLiveData = new MutableLiveData<>();

    PickSeatViewModel(int busId) {
        this.voyageRepository = VoyageRepository.getInstance();
        this.busId = busId;
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    LiveData<SeatRowCollection> getSeats() {
        return voyageRepository.getSeats(busId);
    }


    LiveData<PayDetails> navigateToPay(int pickPoint, int dropPoint, int tripId,
                                       ArrayList<Integer> seats) {
        return voyageRepository.pickSeat(pickPoint, dropPoint, tripId, seats);
    }
}
