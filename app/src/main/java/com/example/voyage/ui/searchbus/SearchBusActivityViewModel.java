package com.example.voyage.ui.searchbus;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.voyage.MainActivity;
import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchBusActivityViewModel extends AndroidViewModel {

    private VoyageRepository voyageRepository;
    private CompositeDisposable disposable = new CompositeDisposable();

    public SearchBusActivityViewModel(@NonNull final Application application) {
        super(application);
        this.voyageRepository = new VoyageRepository();
        VoyageAuth auth = VoyageAuth.getInstance();
        disposable.add(auth.getUserInstance().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(userObserver));
    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }

    public LiveData<List<Schedule>> getSchedules() {
        return voyageRepository.getSchedules();
    }

    private DisposableObserver<VoyageUser> userObserver = new DisposableObserver<VoyageUser>() {
        @Override
        public void onNext(VoyageUser voyageUser) {
            if (voyageUser == null) {
                Intent intent = new Intent(getApplication(), MainActivity.class);
                getApplication().startActivity(intent);
            }
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

        }
    };
}
