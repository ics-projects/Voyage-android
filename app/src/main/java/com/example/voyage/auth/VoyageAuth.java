package com.example.voyage.auth;

import android.util.Log;

import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.example.voyage.data.repositories.VoyageRepository;
import com.example.voyage.fcm.VoyageMessagingService;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;
import com.google.gson.JsonObject;

import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import retrofit2.Response;

public class VoyageAuth implements BaseAuth<VoyageUser> {
    private static final String LOG_TAG = VoyageAuth.class.getSimpleName();
    private static VoyageAuth instance;

    private VoyageService voyageService = VoyageClient.getInstance().getVoyageService();

    private BehaviorSubject<VoyageUser> userSubject = BehaviorSubject.create();

    private VoyageAuth() {
    }

    public static VoyageAuth getInstance() {
        if (instance == null) {
            instance = new VoyageAuth();
        }
        return instance;
    }

    @Override
    public Observable<VoyageUser> signInWithEmailAndPassword(String email, String password) {
        JsonObject postParameters = new JsonObject();
        postParameters.addProperty("email", email);
        postParameters.addProperty("password", password);

        Single.fromObservable(voyageService.login(postParameters))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveUserObserver);

        return userSubject;
    }

    @Override
    public Observable<VoyageUser> createUserWithCredentials(String firstName, String lastName,
                                                            String email, String password,
                                                            String passwordConfirm) {
        JsonObject postParameters = new JsonObject();
        postParameters.addProperty("first_name", firstName);
        postParameters.addProperty("last_name", lastName);
        postParameters.addProperty("email", email);
        postParameters.addProperty("password", password);
        postParameters.addProperty("password_confirmation", passwordConfirm);

        Single.fromObservable(voyageService.register(postParameters))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveUserObserver);

        return userSubject;
    }

    @Override
    public Observable<Response<VoyageUser>> createUserWithEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public void signOut() {
        String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
        PreferenceUtilities.setUserToken(ApplicationContextProvider.getContext(), null);
        if (token != null) {
            String authHeader = "Bearer ".concat(token);
            Completable completable = Completable.fromObservable(voyageService.logout(authHeader));
            Disposable d = completable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Log.d(LOG_TAG, "Logged out successfully");
                        userSubject.onComplete();
                    }, Throwable::printStackTrace);
        }
    }

    public BehaviorSubject<VoyageUser> currentUser() {
        if (userSubject.hasValue()) {
            return userSubject;
        } else {
            String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
            if (token != null) {
                String authHeader = "Bearer ".concat(token);
                Log.d(LOG_TAG, "Stored token: " + token);
                Disposable d = Single.fromObservable(voyageService.getUser(authHeader))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(throwable ->
                                Single.just(Response.success(new VoyageUser(throwable))))
                        .subscribe(response -> {
                            if (response.isSuccessful()) {
                                assert response.body() != null;
                                response.body().setToken(token);
                                userSubject.onNext(response.body());
                                userSubject.onComplete();
                            }
                        }, throwable -> userSubject.onNext(new VoyageUser(throwable)));

    }

    private SingleObserver<Response<VoyageUser>> saveUserObserver =
            new SingleObserver<Response<VoyageUser>>() {
                @Override
                public void onSubscribe(Disposable d) {
                }

                @Override
                public void onSuccess(Response<VoyageUser> voyageUserResponse) {
                    if (voyageUserResponse.isSuccessful()) {
                        assert voyageUserResponse.body() != null;
                        Log.d(LOG_TAG, "User token: ".concat(voyageUserResponse.body().getToken()));
                        PreferenceUtilities.setUserToken(
                                ApplicationContextProvider.getContext(),
                                voyageUserResponse.body().getToken());

                        sendFcmRegistrationToServer();

                        userSubject.onNext(voyageUserResponse.body());
                    } else {
                        try {
                            assert voyageUserResponse.errorBody() != null;
                            Log.d(LOG_TAG, "Error: "
                                    + voyageUserResponse.errorBody().string()
                                    + " Status Code: " + voyageUserResponse.code()
                            );
                            if (voyageUserResponse.code() == 401) {
                                Log.d(LOG_TAG, "Token expired. Logging out user");
                                signOut();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(LOG_TAG, "Error: ", e);
                    e.printStackTrace();
                }
            };

    private void sendFcmRegistrationToServer() {
        String fcmToken = VoyageMessagingService.getToken(ApplicationContextProvider.getContext());
        if (fcmToken != null) {
            VoyageRepository.getInstance().sendFcmToken(fcmToken);
        } else {
            Log.d(LOG_TAG, "Fcm token absent");
        }
    }
}
