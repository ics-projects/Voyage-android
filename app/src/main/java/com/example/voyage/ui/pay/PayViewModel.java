package com.example.voyage.ui.pay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.voyage.data.repositories.VoyageRepository;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PayViewModel extends ViewModel {

    private MutableLiveData<Integer> payRequestStatusLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private VoyageRepository voyageRepository;

    public PayViewModel() {
        voyageRepository = VoyageRepository.getInstance();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    LiveData<Integer> payRequestStatus() {
        return payRequestStatusLiveData;
    }

    void pay(String url, String phoneNumber, int tripId, int pickPoint, int dropPoint,
             ArrayList<Integer> intentSeatIds) {
        compositeDisposable.add(
                voyageRepository.pay(url, phoneNumber, tripId, pickPoint, dropPoint, intentSeatIds)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(status -> {
                            if (status != null) {
                                payRequestStatusLiveData.setValue(status);
                            }
                        }, Throwable::printStackTrace)
        );
    }
}
