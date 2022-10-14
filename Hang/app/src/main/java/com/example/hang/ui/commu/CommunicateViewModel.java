package com.example.hang.ui.commu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CommunicateViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public CommunicateViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("交流");
    }

    public LiveData<String> getText() {
        return mText;
    }
}