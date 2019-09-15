package com.cellgroup.cellapp.ui.editProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditProfileModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EditProfileModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is edit profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}