package com.example.voyage.ui.authentication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivityViewModel extends AndroidViewModel {
    private static final String LOG_TAG = RegisterActivityViewModel.class.getSimpleName();

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();
    private VoyageAuth auth;

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    void registerUser(String firstName, String lastName, String email, String password,
                      String passwordConfirm) {
        auth.createUserWithCredentials(firstName, lastName, email, password, passwordConfirm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VoyageUser>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(VoyageUser voyageUser) {
                        if (voyageUser != null) {
                            Log.d(LOG_TAG, "User: " + voyageUser.getToken());
                            user.setValue(voyageUser);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    MutableLiveData<VoyageUser> getUser() {
        return user;
    }
}
