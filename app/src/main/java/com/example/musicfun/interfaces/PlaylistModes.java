package com.example.musicfun.interfaces;

import com.example.musicfun.datatype.Songs;

import java.util.ArrayList;

public interface PlaylistModes {
    void repeatMode(ArrayList<Songs> listOfSongs, int repeatMode, boolean shuffle);
}
