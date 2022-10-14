package com.example.hang.ui.learn;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LearnViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public LearnViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("学习");
    }

    public LiveData<String> getText() {
        return mText;
    }
}