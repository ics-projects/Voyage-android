package com.example.voyage.ui.pay;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.example.voyage.data.repositories.VoyageRepository;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class PayViewModel extends AndroidViewModel {

    private MutableLiveData<Integer> payRequestStatusLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private VoyageRepository voyageRepository;

    public PayViewModel(@NonNull Application application) {
        super(application);
        voyageRepository = new VoyageRepository();
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
                        .subscribeWith(new DisposableObserver<Integer>() {
                            @Override
                            public void onNext(Integer status) {
                                if (status != null) {
                                    payRequestStatusLiveData.setValue(status);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }));
    }
}
