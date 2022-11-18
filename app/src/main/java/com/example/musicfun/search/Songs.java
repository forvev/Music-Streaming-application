package com.example.musicfun.search;

public class Songs {
    private String songName;
    private String musician;

    public Songs(String songName) {
        this.songName = songName;
        this.musician = "Yes its me!";
    }

    public String getSongName() {
        return this.songName;
    }
    public String getMusician() {
        return this.musician;
    }
}