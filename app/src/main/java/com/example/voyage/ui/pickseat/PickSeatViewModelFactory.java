package com.example.voyage.ui.pickseat;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class PickSeatViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private int mBusId;

    PickSeatViewModelFactory(int busId) {
        this.mBusId = busId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PickSeatViewModel(mBusId);
    }
}
