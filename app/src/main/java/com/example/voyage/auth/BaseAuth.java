package com.example.voyage.auth;

import io.reactivex.Observable;

public interface BaseAuth<T> {
    Observable<T> signInWithEmailAndPassword(String email, String password);

    Observable<T> createUserWithCredentials(String firstName, String lastName, String email,
                                            String password, String passwordConfirm);

    Observable<T> createUserWithEmailAndPassword(String email, String password);

    Observable<T> currentUser();

    Observable<T> signOut();
}
