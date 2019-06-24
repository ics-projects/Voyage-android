package com.example.voyage.auth;

import android.util.Log;

import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;
import com.google.gson.JsonObject;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VoyageAuth implements BaseAuth<VoyageUser> {
    private static final String LOG_TAG = VoyageAuth.class.getSimpleName();
    private static VoyageAuth instance;
    private static VoyageUser userInstance;

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
    public Observable<Response<VoyageUser>> signInWithEmailAndPassword(String email, String password) {
        if (currentUser() != null) {
            return Observable.just(Response.success(userInstance));
        }

        JsonObject postParameters = new JsonObject();
        postParameters.addProperty("email", email);
        postParameters.addProperty("password", password);

        Observable<Response<VoyageUser>> user = voyageService.login(postParameters);
        setUserInstance(user);
        return user;
    }

    @Override
    public Observable<Response<VoyageUser>> createUserWithCredentials(String firstName, String lastName,
                                                                      String email, String password,
                                                                      String passwordConfirm) {
        JsonObject postParameters = new JsonObject();
        postParameters.addProperty("first_name", firstName);
        postParameters.addProperty("last_name", lastName);
        postParameters.addProperty("email", email);
        postParameters.addProperty("password", password);
        postParameters.addProperty("password_confirmation", passwordConfirm);

        Observable<Response<VoyageUser>> user = voyageService.register(postParameters);
        setUserInstance(user);
        return user;
    }

    @Override
    public Observable<Response<VoyageUser>> createUserWithEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public Observable<Response<VoyageUser>> signOut() {
//        String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
//        if (token != null) {
//            String authHeader = "Bearer ".concat(token);
//            userInstance = null;
//            Observable<Response<VoyageUser>> logoutResponse = voyageService.logout(authHeader);
//            return logoutResponse;
//        }

        userInstance = null;
        PreferenceUtilities.setUserToken(ApplicationContextProvider.getContext(), null);
        return null;
    }

    public Observable<Response<VoyageUser>> currentUser() {
        if (userInstance != null) {
            return Observable.just(Response.success(userInstance));
        } else {
            String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
            if (token != null) {
                String authHeader = "Bearer ".concat(token);
                Log.d(LOG_TAG, "auth header: " + authHeader);
                Observable<Response<VoyageUser>> user = voyageService.getUser(authHeader);
                setUserInstance(user);
                return user;
            } else return null;
        }
    }

    public Observable<VoyageUser> getUserInstance() {
        if (userInstance == null) {
            if (currentUser() != null) {
                return currentUser().map(Response::body);
            }
        } else {
            return Observable.just(userInstance);
        }
        return null;
    }

    private void setUserInstance(final Observable<Response<VoyageUser>> user) {
        user.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userObserver);
    }

    private Observer<Response<VoyageUser>> userObserver = new Observer<Response<VoyageUser>>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Response<VoyageUser> voyageUserResponse) {
            if (voyageUserResponse.isSuccessful()) {
                userInstance = voyageUserResponse.body();
                PreferenceUtilities.setUserToken(
                        ApplicationContextProvider.getContext(), userInstance.getToken());
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
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
}
