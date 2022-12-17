package com.example.musicfun.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.musicfun.datatype.Songs;

import java.util.ArrayList;

public class MainActivityViewModel extends AndroidViewModel {


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
    }


    public ArrayList<Songs> getListOfSongs(){
        ArrayList<Songs> playlist = new ArrayList<>();
        playlist.add(new Songs ("Song 1", "Artist 1", 1));
        playlist.add(new Songs ("Song 2", "Artist 2", 2));
        playlist.add(new Songs ("Song 3", "Artist 3", 3));
        playlist.add(new Songs ("Song 4", "Artist 4", 4));
        playlist.add(new Songs ("Song 5", "Artist 5", 5));
        playlist.add(new Songs ("Song 6", "Artist 6", 6));
        return playlist;
    }
}
