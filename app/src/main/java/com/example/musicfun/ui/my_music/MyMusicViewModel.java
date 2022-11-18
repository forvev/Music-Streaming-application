package com.example.musicfun.ui.my_music;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyMusicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MyMusicViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is my music fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}