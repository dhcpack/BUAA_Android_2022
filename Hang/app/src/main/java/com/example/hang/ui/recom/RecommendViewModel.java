package com.example.hang.ui.recom;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecommendViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private final MutableLiveData<String> mText;

    public RecommendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("推荐");
    }

    public LiveData<String> getText() {
        return mText;
    }
}