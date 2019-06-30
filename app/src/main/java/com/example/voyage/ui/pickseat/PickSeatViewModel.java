package com.example.voyage.ui.pickseat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.voyage.data.models.PayDetails;
import com.example.voyage.data.models.Seat;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PickSeatViewModel extends AndroidViewModel {
    private final VoyageRepository voyageRepository;

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<SeatRowCollection> seatRowCollectionLiveData = new MutableLiveData<>();

    PickSeatViewModel(@NonNull Application application, int busId) {
        super(application);
        this.voyageRepository = new VoyageRepository();
        getSeatsFromRepository(busId);
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    private void getSeatsFromRepository(int busId) {
        voyageRepository.getSeats(busId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Seat>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(List<Seat> seats) {
                        assert seats != null;
                        SeatRowCollection rowCollection = new SeatRowCollection();
                        for (int i = 0; i < seats.size(); i += 4) {
                            List<Seat> seatRow = new ArrayList<>(seats.subList(i, i + 4));
                            rowCollection.add(seatRow);
                        }
                        seatRowCollectionLiveData.setValue(rowCollection);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    LiveData<SeatRowCollection> getSeats() {
        return seatRowCollectionLiveData;
    }


    LiveData<PayDetails> navigateToPay(int pickPoint, int dropPoint, int tripId, ArrayList<Integer> seats) {
        return voyageRepository.pickSeat(pickPoint, dropPoint, tripId, seats);
    }
}
