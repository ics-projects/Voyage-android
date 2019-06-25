package com.example.voyage.ui.searchbus;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.models.Trip;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SearchBusActivityViewModel extends AndroidViewModel {

    private VoyageRepository voyageRepository;
    private CompositeDisposable disposables = new CompositeDisposable();

    public SearchBusActivityViewModel(@NonNull final Application application) {
        super(application);
        this.voyageRepository = new VoyageRepository();
        VoyageAuth auth = VoyageAuth.getInstance();
//        disposables.add(auth.currentUser().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(userObserver));
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }

    public LiveData<List<Schedule>> getSchedules() {
        return voyageRepository.getSchedules();
    }

    public LiveData<List<Trip>> getTrips(String origin, String destination, String date) {
        return voyageRepository.getTrips(origin, destination, date);
    }

//    private DisposableObserver<VoyageUser> userObserver = new DisposableObserver<VoyageUser>() {
//        @Override
//        public void onNext(VoyageUser voyageUser) {
//            if (voyageUser == null) {
//                Intent intent = new Intent(getApplication(), MainActivity.class);
//                getApplication().startActivity(intent);
//            }
//        }
//
//        @Override
//        public void onError(Throwable e) {
//
//        }
//
//        @Override
//        public void onComplete() {
//
//        }
//    };
}
