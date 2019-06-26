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

class LoginActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = LoginActivityViewModel.class.getSimpleName();

    private CompositeDisposable disposables = new CompositeDisposable();
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();

    private VoyageAuth auth;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    MutableLiveData<VoyageUser> getUser() {
        return user;
    }

    void loginUser(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
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
                            Log.d(LOG_TAG, "User token: ".concat(voyageUser.getToken()));
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
}
