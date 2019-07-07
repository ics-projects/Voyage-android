package com.example.voyage.ui.searchbus;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.voyage.auth.VoyageAuth;
import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.repositories.VoyageRepository;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class SearchBusActivityViewModel extends ViewModel {

    private static final String LOG_TAG = SearchBusActivityViewModel.class.getSimpleName();
    private final VoyageAuth auth;
    private VoyageRepository voyageRepository;
    private MutableLiveData<VoyageUser> user = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public SearchBusActivityViewModel() {
        this.voyageRepository = VoyageRepository.getInstance();
        this.auth = VoyageAuth.getInstance();
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    LiveData<List<Schedule>> getSchedules() {
        return voyageRepository.getSchedules();
    }

    LiveData<VoyageUser> getUser() {
        return user;
    }

    void signOut() {
        auth.signOut();
        user.setValue(null);
    }
}
