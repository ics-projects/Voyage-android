package com.example.voyage.ui.authentication;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

class LoginActivityViewModel extends ViewModel {

    private static final String LOG_TAG = LoginActivityViewModel.class.getSimpleName();

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();

    private VoyageAuth auth;

    public LoginActivityViewModel() {
        auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    LiveData<VoyageUser> getUser() {
        return user;
    }

    void loginUser(String email, String password) {
        disposables.add(
                auth.signInWithEmailAndPassword(email, password)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((voyageUser) -> {
                            if (voyageUser != null) {
                                Log.d(LOG_TAG, "User token: ".concat(voyageUser.getToken()));
                                user.setValue(voyageUser);
                            }
                        }, Throwable::printStackTrace)
        );
    }
}
