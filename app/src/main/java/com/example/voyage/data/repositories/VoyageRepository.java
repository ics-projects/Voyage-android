package com.example.voyage.data.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;
import android.widget.Toast;

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
import com.example.voyage.ui.pickseat.SeatRowCollection;
import com.example.voyage.util.ApplicationContextProvider;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

public class VoyageRepository {
    private static final String LOG_TAG = VoyageRepository.class.getSimpleName();
    private static VoyageRepository instance;

    private VoyageService voyageService;
    private BehaviorSubject<VoyageUser> voyageUser;

    private MutableLiveData<List<Schedule>> schedules = new MutableLiveData<>();
    private MutableLiveData<List<Trip>> trips = new MutableLiveData<>();
    private MutableLiveData<SeatRowCollection> seats = new MutableLiveData<>();
    private MutableLiveData<PayDetails> payDetails = new MutableLiveData<>();
    private MutableLiveData<Integer> payStatus = new MutableLiveData<>();

    private AtomicReference<String> authToken = new AtomicReference<>("");

    private VoyageRepository() {
        voyageService = VoyageClient.getInstance().getVoyageService();
        voyageUser = VoyageAuth.getInstance().currentUser();
    }

    public static VoyageRepository getInstance() {
        if (instance == null) {
            instance = new VoyageRepository();
        }
        return instance;
    }

    public LiveData<List<Schedule>> getSchedules() {

        Disposable disposable = getUserResponseSingle(
                getSingleSourceFunction(voyageService::schedules))
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        schedules.postValue(response.body());
                    } else {
                        if (response.code() == 401) {
                            voyageService.logout(authToken.get());
                        }
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, throwable -> {
                    handleError(throwable);
                    schedules.setValue(null);
                });

        return schedules;
    }

    public LiveData<List<Trip>> getTrips(String origin, String destination, String date) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("departure", origin);
        jsonObject.addProperty("destination", destination);
        jsonObject.addProperty("date", date);

        Disposable d = getUserResponseSingle(
                getSingleSourceFunctionWithBody(voyageService::trips, jsonObject))
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        trips.postValue(response.body());
                    } else {
                        if (response.code() == 401) {
                            voyageService.logout(authToken.get());
                        }
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, throwable -> {
                    handleError(throwable);
                    trips.setValue(null);
                });


        return trips;
    }

    public LiveData<SeatRowCollection> getSeats(int busId) {

        Disposable disposable = getUserResponseSingle(
                getSingleSourceFunctionWithBody(voyageService::seats, busId))
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        assert response.body() != null;

                        // Push seats to row collection
                        SeatRowCollection rowCollection = new SeatRowCollection();
                        for (int i = 0; i < response.body().size(); i += 4) {
                            List<Seat> seatRow = new ArrayList<>(response.body().subList(i, i + 4));
                            rowCollection.add(seatRow);
                            seats.setValue(rowCollection);
                        }

                    } else {
                        if (response.code() == 401) {
                            voyageService.logout(authToken.get());
                        }
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, throwable -> {
                    handleError(throwable);
                    seats.setValue(null);
                });

        return seats;
    }

    public LiveData<PayDetails> pickSeat(int pickPoint, int dropPoint, int tripId,
                                         ArrayList<Integer> seats) {
        PickSeatBody seatBody = new PickSeatBody(pickPoint, dropPoint, tripId, seats);

        Disposable disposable = getUserResponseSingle(
                getSingleSourceFunctionWithBody(voyageService::pickSeat, seatBody))
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        payDetails.postValue(response.body());
                    } else {
                        if (response.code() == 401) {
                            voyageService.logout(authToken.get());
                        }
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, throwable -> {
                    handleError(throwable);
                    payDetails.setValue(null);
                });


        return payDetails;
    }

    public LiveData<Integer> pay(String url, String phoneNumber, int tripId, int pickPoint,
                                 int dropPoint, ArrayList<Integer> intentSeatIds) {

        PayRequestBody payRequestBody = new PayRequestBody(phoneNumber,
                pickPoint, dropPoint, tripId, intentSeatIds);

        Disposable d = getUserResponseSingle(
                getSingleSourceFunctionWithUrl(voyageService::pay, url, payRequestBody))
                .subscribe((response) -> {
                    if (response.isSuccessful()) {
                        if (response.code() == 200)
                            payStatus.setValue(0);
                    } else {
                        if (response.code() == 401) {
                            voyageService.logout(authToken.get());
                        }
                        try {
                            assert response.errorBody() != null;
                            Log.d(LOG_TAG, "Error: " +
                                    response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, throwable -> {
                    handleError(throwable);
                    payStatus.setValue(null);
                });

        return payStatus;
    }

    private <T, U> Function<VoyageUser, SingleSource<? extends Response<T>>> getSingleSourceFunctionWithUrl
            (Function3<String, String, U, Observable<Response<T>>> observableFunction, String url,
             U requestBody) {

        return (user) -> {
            String authToken = "Bearer ".concat(user.getToken());
            return Single.fromObservable(
                    observableFunction.apply(url, authToken, requestBody)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()));
        };
    }

    private <T> Function<VoyageUser, SingleSource<? extends Response<T>>> getSingleSourceFunction
            (Function<String, Observable<Response<T>>> observableFunction) {

        return (user) -> {
            String authToken = "Bearer ".concat(user.getToken());
            return Single.fromObservable(
                    observableFunction.apply(authToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
            );
        };
    }

    private <T, U> Function<VoyageUser, SingleSource<? extends Response<T>>> getSingleSourceFunctionWithBody
            (BiFunction<String, U, Observable<Response<T>>> observableFunction, U data) {

        return (user) -> {
            String authToken = "Bearer ".concat(user.getToken());
            return Single.fromObservable(
                    observableFunction.apply(authToken, data)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
            );
        };
    }

    private <T> Single<Response<T>> getUserResponseSingle
            (Function<VoyageUser, SingleSource<? extends Response<T>>> voyageUserSingleSourceFunction) {

        return voyageUser.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(voyageUserSingleSourceFunction);
    }

    private void handleError(Throwable throwable) {
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            int statusCode = httpException.code();
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Http error. Status code: " + statusCode,
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG_TAG, "Http error. Status code: " + statusCode);
        } else if (throwable instanceof SocketTimeoutException) {
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Connection timed out. Please try again",
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG_TAG, throwable.getMessage());
        } else if (throwable instanceof IOException) {
            Toast.makeText(ApplicationContextProvider.getContext(),
                    "Host unreachable. Check your internet connection",
                    Toast.LENGTH_LONG)
                    .show();
            Log.d(LOG_TAG, throwable.getMessage());
        }
    }
}
