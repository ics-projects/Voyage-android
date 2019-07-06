package com.example.voyage.ui.pickseat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

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
