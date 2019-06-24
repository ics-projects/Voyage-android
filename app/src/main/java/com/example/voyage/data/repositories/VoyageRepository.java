package com.example.voyage.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VoyageRepository {
    private static final String LOG_TAG = VoyageRepository.class.getSimpleName();
    private VoyageService voyageService;
    private Observable<VoyageUser> voyageUser;
    private MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>();

    private VoyageAuth auth = VoyageAuth.getInstance();

    public VoyageRepository() {
        voyageService = VoyageClient.getInstance().getVoyageService();
        voyageUser = auth.getUserInstance();
        voyageUser.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(userObserver);
    }

    private void setSchedules(VoyageUser voyageUser) {
        String authToken = "Bearer ".concat(voyageUser.getToken());
        Log.d(LOG_TAG, "AUTH TOKEN: ".concat(voyageUser.getToken()));
        voyageService.schedules(authToken).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Response<List<Schedule>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<Schedule>> listResponse) {
                        if (listResponse.isSuccessful()) {
                            schedules.setValue(listResponse.body());
                        } else {
                            try {
                                Log.d(LOG_TAG, "Error: " + listResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
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

    public LiveData<List<Schedule>> getSchedules() {
        return schedules;
    }

    private DisposableObserver<VoyageUser> userObserver = new DisposableObserver<VoyageUser>() {
        @Override
        public void onNext(VoyageUser voyageUser) {
            if (voyageUser != null) {
                setSchedules(voyageUser);
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
}
