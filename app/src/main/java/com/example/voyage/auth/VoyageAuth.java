package com.example.voyage.auth;

import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;
import com.google.gson.JsonObject;

import io.reactivex.Observable;

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
    public Observable<VoyageUser> signInWithEmailAndPassword(String email, String password) {
        return voyageService.login(email, password);
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

        return voyageService.register(postParameters);
    }

    @Override
    public Observable<VoyageUser> createUserWithEmailAndPassword(String email, String password) {
        return null;
    }

    @Override
    public Observable<VoyageUser> currentUser() {
        if (userInstance == null) {
            String token = PreferenceUtilities.getUserToken(ApplicationContextProvider.getContext());
            if (token != null) {
                userInstance = new VoyageUser(token);
                return Observable.just(userInstance);
            }
        } else return Observable.just(userInstance);

        return null;
    }

    @Override
    public Observable<VoyageUser> signOut() {
        return null;
    }
}
