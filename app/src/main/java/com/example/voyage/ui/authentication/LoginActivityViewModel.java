package com.example.voyage.ui.authentication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

class LoginActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = LoginActivityViewModel.class.getSimpleName();

    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();

    private VoyageAuth auth;

    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    public MutableLiveData<VoyageUser> getUser() {
        return user;
    }

    public void loginUser(String email, String password) {
        disposable.add(auth.signInWithEmailAndPassword(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<VoyageUser>>() {
                    @Override
                    public void onNext(Response<VoyageUser> voyageUserResponse) {
                        if (voyageUserResponse.isSuccessful()) {
                            user.setValue(voyageUserResponse.body());
                            Log.d(LOG_TAG, "User logged in: " + voyageUserResponse.body().getToken());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }
}
