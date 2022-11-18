package com.example.musicfun.ui.discovery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.musicfun.search.Songs;

import java.util.ArrayList;

public class DiscoveryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    MutableLiveData<ArrayList<Songs>> songNames;
    ArrayList<Songs> songsArrayList = new ArrayList<>();

    public DiscoveryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is discovery fragment");

        songNames = new MutableLiveData<>();
        init();
    }

    public MutableLiveData<ArrayList<Songs>> getSongNames(){
        return songNames;
    }

    // TODO: fetch data from database
    public void init(){
        populateList();
        songNames.setValue(songsArrayList);
    }

    // TODO: fetch data from database
    public void populateList(){
        String[] temp = new String[]{"AAAAAA", "BBBBBBB", "C", "D","a", "b", "c", "d","aa", "bb", "cc", "dd","A", "B", "C", "D","A", "B", "C", "D","A", "B", "C", "D","A", "B", "C", "D"};
        for (int i = 0; i < temp.length; i++) {
            Songs songs = new Songs(temp[i]);
            // Binds all strings into an array
            songsArrayList.add(songs);
        }
    }

    public LiveData<String> getText() {
        return mText;
    }
}