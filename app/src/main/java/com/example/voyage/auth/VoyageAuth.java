package com.example.voyage.auth;

import com.example.voyage.data.network.retrofit.VoyageClient;
import com.example.voyage.data.network.retrofit.VoyageService;
import com.google.gson.JsonObject;

import io.reactivex.Observable;

public class VoyageAuth implements BaseAuth<VoyageUser> {
    private static VoyageAuth instance;

    VoyageService voyageService = VoyageClient.getInstance().getVoyageService();

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
        return null;
    }

    @Override
    public Observable<VoyageUser> signOut() {
        return null;
    }
}
