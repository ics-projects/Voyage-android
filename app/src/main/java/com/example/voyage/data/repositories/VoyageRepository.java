package com.example.voyage.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.PayDetails;
import com.example.voyage.data.models.PayRequestBody;
import com.example.voyage.data.models.PickSeatBody;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.models.Seat;
import com.example.voyage.data.models.Trip;
import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class VoyageRepository {
    private static final String LOG_TAG = VoyageRepository.class.getSimpleName();
    private static VoyageRepository instance;

    private VoyageService voyageService;
    private Single<VoyageUser> voyageUser;

    private MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>();
    private MutableLiveData<List<Trip>> trips = new MutableLiveData<>();
    private BehaviorSubject<List<Seat>> seats = BehaviorSubject.create();
    private MutableLiveData<PayDetails> payDetails = new MutableLiveData<>();
    private BehaviorSubject<Integer> payStatus = BehaviorSubject.create();

    private VoyageRepository() {
        voyageService = VoyageClient.getInstance().getVoyageService();
        voyageUser = Single.fromObservable(VoyageAuth.getInstance().currentUser());
    }

    public static VoyageRepository getInstance() {
        if (instance == null) {
            instance = new VoyageRepository();
        }
        return instance;
    }

    public LiveData<List<Schedule>> getSchedules() {

        Disposable disposable = voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((user) -> {
                    String authToken = "Bearer ".concat(user.getToken());
                    return Single.fromObservable(
                            voyageService.schedules(authToken)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                    );
                })
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        schedules.postValue(response.body());
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, Throwable::printStackTrace);

        return schedules;
    }

    public LiveData<List<Trip>> getTrips(String origin, String destination, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departure", origin);
        jsonObject.addProperty("destination", destination);
        jsonObject.addProperty("date", date);

        Disposable disposable = voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((user) -> {
                    String authToken = "Bearer ".concat(user.getToken());
                    return Single.fromObservable(
                            voyageService.trips(authToken, jsonObject)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()));
                })
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        trips.postValue(response.body());
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, Throwable::printStackTrace);


        return trips;
    }

    public Observable<List<Seat>> getSeats(int busId) {

        Disposable disposable = voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((user) -> {
                    String authToken = "Bearer ".concat(user.getToken());
                    return Single.fromObservable(
                            voyageService.seats(authToken, busId)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()));
                })
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        seats.onNext(response.body());
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, Throwable::printStackTrace);

        return seats;
    }

    public LiveData<PayDetails> pickSeat(int pickPoint, int dropPoint, int tripId,
                                         ArrayList<Integer> seats) {
        PickSeatBody seatBody = new PickSeatBody(pickPoint, dropPoint, tripId, seats);

        Disposable disposable = voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((user) -> {
                    String authToken = "Bearer ".concat(user.getToken());
                    return Single.fromObservable(
                            voyageService.pickSeat(authToken, seatBody)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()));
                })
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        payDetails.postValue(response.body());
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, Throwable::printStackTrace);


        return payDetails;
    }

    public Observable<Integer> pay(String url, String phoneNumber, int tripId, int pickPoint,
                                   int dropPoint, ArrayList<Integer> intentSeatIds) {
        PayRequestBody payRequestBody = new PayRequestBody(phoneNumber,
                pickPoint, dropPoint, tripId, intentSeatIds);
        Disposable disposable = voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((user) -> {
                    String authToken = "Bearer ".concat(user.getToken());

                    return Single.fromObservable(
                            voyageService.pay(url, authToken, payRequestBody)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread()));
                })
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        if (response.code() == 200)
                            payStatus.onNext(0);
                    } else {
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, Throwable::printStackTrace);

        return payStatus;
    }
}
