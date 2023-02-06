package com.example.musicfun.datatype;

public class Songs {
    private String songName;
    private String artist;
    private String id;

    public Songs(String songName, String artist, String id) {
        this.songName = songName;
        this.artist = artist;
        this.id = id;
    }

    public String getSongName() {
        return this.songName;
    }
    public String getArtist() {
        return this.artist;
    }
    public String getSongId() {
        return this.id;
    }
}