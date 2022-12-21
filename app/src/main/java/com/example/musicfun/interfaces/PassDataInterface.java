package com.example.musicfun.interfaces;

import com.example.musicfun.datatype.Songs;

import java.util.List;

public interface PassDataInterface {
/*
    Pass a list of songs, repeat mode, and shuffle to the main activity to start playing songs
 */
    void playSong(List<Songs> playlist, int repeatMode, boolean shuffle);
}
