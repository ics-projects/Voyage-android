package com.example.voyage.auth;

import io.reactivex.Observable;
import retrofit2.Response;

public interface BaseAuth<T> {
    Observable<VoyageUser> signInWithEmailAndPassword(String email, String password);

    Observable<VoyageUser> createUserWithCredentials(String firstName, String lastName, String email,
                                                     String password, String passwordConfirm);

    Observable<Response<T>> createUserWithEmailAndPassword(String email, String password);

//    Observable<T> currentUser();

    void signOut();
}
