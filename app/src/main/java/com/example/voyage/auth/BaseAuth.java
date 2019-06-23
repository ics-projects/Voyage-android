package com.example.voyage.auth;

import io.reactivex.Observable;
import retrofit2.Response;

public interface BaseAuth<T> {
    Observable<Response<T>> signInWithEmailAndPassword(String email, String password);

    Observable<Response<T>> createUserWithCredentials(String firstName, String lastName, String email,
                                            String password, String passwordConfirm);

    Observable<Response<T>> createUserWithEmailAndPassword(String email, String password);

//    Observable<T> currentUser();

    Observable<Response<T>> signOut();
}
