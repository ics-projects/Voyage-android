package com.example.voyage.auth;

import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class VoyageAuth implements BaseAuth<VoyageUser> {
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
        Observable<Response<VoyageUser>> user = voyageService.login(email, password);
//        setUserInstance(Single.fromObservable(user));
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
//        setUserInstance(Single.fromObservable(user));
        return user;
    }

    @Override
    public Observable<Response<VoyageUser>> createUserWithEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public Observable<Response<VoyageUser>> signOut() {
        return null;
    }

    public VoyageUser currentUser() {
        if (userInstance == null) {
            String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
            if (token != null) {
                Observable<Response<VoyageUser>> user = voyageService.getUser("Bearer ".concat(token));
                setUserInstance(Single.fromObservable(user));
            }
        }

        return userInstance;
    }

    private void setUserInstance(Single<Response<VoyageUser>> user) {
        user.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<VoyageUser>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<VoyageUser> voyageUserResponse) {
                        if (voyageUserResponse.isSuccessful()) {
                            userInstance = voyageUserResponse.body();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}
