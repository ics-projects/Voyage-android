package com.example.voyage.auth;

import android.util.Log;

import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;
import com.google.gson.JsonObject;

import java.io.IOException;

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
    private static BehaviorSubject<VoyageUser> userPublishSubject = BehaviorSubject.create();

    private VoyageService voyageService = VoyageClient.getInstance().getVoyageService();

    private VoyageAuth() {
    }

    public static VoyageAuth getInstance() {
        if (instance == null) {
            instance = new VoyageAuth();
        }
        return instance;
    }

    @Override
    public BehaviorSubject<VoyageUser> signInWithEmailAndPassword(String email, String password) {
        JsonObject postParameters = new JsonObject();
        postParameters.addProperty("email", email);
        postParameters.addProperty("password", password);

        Observable<Response<VoyageUser>> user = voyageService.login(postParameters);
        Single.fromObservable(user).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveUserObserver);
        return userPublishSubject;
    }

    @Override
    public BehaviorSubject<VoyageUser> createUserWithCredentials(String firstName, String lastName,
                                                                 String email, String password,
                                                                 String passwordConfirm) {
        JsonObject postParameters = new JsonObject();
        postParameters.addProperty("first_name", firstName);
        postParameters.addProperty("last_name", lastName);
        postParameters.addProperty("email", email);
        postParameters.addProperty("password", password);
        postParameters.addProperty("password_confirmation", passwordConfirm);

        Observable<Response<VoyageUser>> user = voyageService.register(postParameters);
        Single.fromObservable(user.subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveUserObserver);
        return userPublishSubject;
    }

    @Override
    public Observable<Response<VoyageUser>> createUserWithEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public Observable<Response<VoyageUser>> signOut() {
        PreferenceUtilities.setUserToken(ApplicationContextProvider.getContext(), null);
        return null;
    }

    public BehaviorSubject<VoyageUser> currentUser() {
        if (userPublishSubject.getValue() != null) {
            return userPublishSubject;
        } else {
            String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
            if (token != null) {
                String authHeader = "Bearer ".concat(token);
                Log.d(LOG_TAG, "Stored token: " + token);
                Single.fromObservable(voyageService.getUser(authHeader))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<VoyageUser>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(Response<VoyageUser> voyageUserResponse) {
                                if (voyageUserResponse.isSuccessful()) {
                                    voyageUserResponse.body().setToken(token);
                                    userPublishSubject.onNext(voyageUserResponse.body());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        });
                return userPublishSubject;
            } else return null;
        }
    }

    private SingleObserver<Response<VoyageUser>> saveUserObserver = new SingleObserver<Response<VoyageUser>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(Response<VoyageUser> voyageUserResponse) {
            if (voyageUserResponse.isSuccessful()) {
                Log.d(LOG_TAG, "User token: ".concat(voyageUserResponse.body().getToken()));
                PreferenceUtilities.setUserToken(
                        ApplicationContextProvider.getContext(), voyageUserResponse.body().getToken());
                userPublishSubject.onNext(voyageUserResponse.body());
            } else {
                try {
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
}
