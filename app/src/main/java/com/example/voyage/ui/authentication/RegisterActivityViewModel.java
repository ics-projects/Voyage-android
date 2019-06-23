package com.example.voyage.ui.authentication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.util.ApplicationContextProvider;
import com.example.voyage.util.PreferenceUtilities;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RegisterActivityViewModel extends AndroidViewModel {
    private static final String LOG_TAG = RegisterActivityViewModel.class.getSimpleName();
    private CompositeDisposable disposable = new CompositeDisposable();

    private VoyageAuth auth;
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();

    public RegisterActivityViewModel(@NonNull Application application) {
        super(application);
        auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    public void registerUser(String firstName, String lastName, String email, String password,
                             String passwordConfirm) {
        disposable.add(auth.createUserWithCredentials(firstName, lastName, email, password, passwordConfirm)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<VoyageUser>>() {

                    @Override
                    public void onNext(Response<VoyageUser> voyageUserResponse) {
                        if (voyageUserResponse.isSuccessful()) {
                            user.setValue(voyageUserResponse.body());
                            Log.d(LOG_TAG,
                                    "User: ".concat(voyageUserResponse.body().getToken()));
                        } else {
                            try {
                                Log.d(LOG_TAG, "Error: " + voyageUserResponse.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    public MutableLiveData<VoyageUser> getUser() {
        return user;
    }
}
