package com.example.voyage.ui.authentication;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivityViewModel extends ViewModel {
    private static final String LOG_TAG = RegisterActivityViewModel.class.getSimpleName();

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();
    private VoyageAuth auth;

    public RegisterActivityViewModel() {
        auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    void registerUser(String firstName, String lastName, String email, String password,
                      String passwordConfirm) {
        disposables.add(
                auth.createUserWithCredentials(firstName, lastName, email, password, passwordConfirm)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((voyageUser) -> {
                            if (voyageUser != null) {
                                Log.d(LOG_TAG, "User: " + voyageUser.getToken());
                                user.setValue(voyageUser);
                            }
                        }, Throwable::printStackTrace)
        );
    }

    LiveData<VoyageUser> getUser() {
        return user;
    }
}
