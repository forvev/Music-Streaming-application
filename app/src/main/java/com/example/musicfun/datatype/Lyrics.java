package com.example.musicfun.datatype;

public class Lyrics {

    private long startTime;
    private String lyrics;
    private int length;

    public Lyrics(long startTime, String lyrics) {
        this.startTime = startTime;
        this.lyrics = lyrics;
        this.length = lyrics.length();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
