package com.example.voyage.ui.pickseat;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class PickSeatViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private final int mBusId;
    private final Application mApplication;

    /**
     * Creates a {@code AndroidViewModelFactory}
     *
     * @param application an application to pass in {@link AndroidViewModel}
     * @param mBusId      bus id to be used in view model
     */
    public PickSeatViewModelFactory(@NonNull Application application, int mBusId) {
        super(application);
        this.mApplication = application;
        this.mBusId = mBusId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PickSeatViewModel(mApplication, mBusId);
    }
}
