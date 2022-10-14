package com.example.hang.ui.mine;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MineViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public MineViewModel(){
        mText = new MutableLiveData<>();
        mText.setValue("我的");
    }
}