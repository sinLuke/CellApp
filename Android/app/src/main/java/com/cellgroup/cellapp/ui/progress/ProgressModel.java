package com.cellgroup.cellapp.ui.progress;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProgressModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is progress fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}