package com.example.voyage.ui.searchbus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class SearchBusActivityViewModel extends ViewModel {

    private static final String LOG_TAG = SearchBusActivityViewModel.class.getSimpleName();
    private final VoyageAuth auth;
    private VoyageRepository voyageRepository;
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchBusActivityViewModel() {
        this.voyageRepository = VoyageRepository.getInstance();
        this.auth = VoyageAuth.getInstance();
        getUserInstance();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    private void getUserInstance() {
        BehaviorSubject<VoyageUser> voyageUserBehaviorSubject = auth.currentUser();
        if (voyageUserBehaviorSubject.hasComplete()) {
            user.setValue(null);
        } else {
            compositeDisposable.add(
                    voyageUserBehaviorSubject
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnComplete(() -> user.setValue(null))
                            .subscribe(voyageUser -> {
                                if (voyageUser != null) {
                                    user.setValue(voyageUser);
                                }
                            }, Throwable::printStackTrace)
            );
        }

    }

    LiveData<List<Schedule>> getSchedules() {
        return voyageRepository.getSchedules();
    }

    LiveData<VoyageUser> getUser() {
        return user;
    }

    void signOut() {
        auth.signOut();
    }
}
