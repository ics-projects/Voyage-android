package com.example.voyage.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.models.Seat;
import com.example.voyage.data.models.Trip;
import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Response;

public class VoyageRepository {
    private static final String LOG_TAG = VoyageRepository.class.getSimpleName();

    private VoyageService voyageService;
    private BehaviorSubject<VoyageUser> voyageUser = VoyageAuth.getInstance().currentUser();

    private MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>();
    private MutableLiveData<List<Trip>> trips = new MutableLiveData<>();
    private MutableLiveData<List<Seat>> seats = new MutableLiveData<>();

    public VoyageRepository() {
        voyageService = VoyageClient.getInstance().getVoyageService();
    }

    public LiveData<List<Schedule>> getSchedules() {
        voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VoyageUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(VoyageUser voyageUser) {
                        Log.d(LOG_TAG, "Schedules called----------------------");
                        if (voyageUser != null) {
                            assert voyageUser.getToken() != null;
                            Log.d(LOG_TAG, "Retrieved token: " + voyageUser.getToken());
                            String authToken = "Bearer ".concat(voyageUser.getToken());
                            Single.fromObservable(voyageService.schedules(authToken))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<Response<List<Schedule>>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Response<List<Schedule>> listResponse) {
                                            if (listResponse.isSuccessful()) {
                                                schedules.setValue(listResponse.body());
                                            } else {
                                                try {
                                                    assert listResponse.errorBody() != null;
                                                    Log.d(LOG_TAG, "Error: " +
                                                            listResponse.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }
                                    });
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
        return schedules;
    }

    public LiveData<List<Trip>> getTrips(String origin, String destination, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departure", origin);
        jsonObject.addProperty("destination", destination);
        jsonObject.addProperty("date", date);

        voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VoyageUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(VoyageUser voyageUser) {
                        Log.d(LOG_TAG, "Trips called----------------------");
                        if (voyageUser != null) {
                            String authToken = "Bearer ".concat(voyageUser.getToken());

                            Single.fromObservable(voyageService.trips(authToken, jsonObject))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<Response<List<Trip>>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Response<List<Trip>> listResponse) {
                                            if (listResponse.isSuccessful()) {
                                                assert listResponse.body() != null;
                                                Log.d(LOG_TAG, "Length: " +
                                                        listResponse.body().size());

                                                trips.setValue(listResponse.body());
                                            } else {
                                                try {
                                                    assert listResponse.errorBody() != null;
                                                    Log.d(LOG_TAG, "Error: " +
                                                            listResponse.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return trips;
    }

    public LiveData<List<Seat>> getSeats(int tripId) {
        voyageUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VoyageUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(VoyageUser voyageUser) {
                        if (voyageUser != null) {
                            String authToken = "Bearer ".concat(voyageUser.getToken());
                            Log.d(LOG_TAG, "Retrieved token: " + voyageUser.getToken());
                            Single.fromObservable(voyageService.seats(authToken, tripId))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new SingleObserver<Response<List<Seat>>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(Response<List<Seat>> listResponse) {
                                            if (listResponse.isSuccessful()) {
                                                seats.setValue(listResponse.body());
                                            }
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
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
        return seats;
    }
}
