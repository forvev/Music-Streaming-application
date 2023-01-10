package com.example.musicfun.datatype;

public class Playlist {
    private String playlist_name;
    private String playlist_id;
    private String owner;
    private boolean isDefault;

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public void setPlaylist_name(String playlist_name) {
        this.playlist_name = playlist_name;
    }

    public void setPlaylist_id(String playlist_id) {
        this.playlist_id = playlist_id;
    }

    public Playlist(String playlist_name, String playlist_id, String owner, boolean isDefault) {
        this.playlist_name = playlist_name;
        this.playlist_id = playlist_id;
        this.isDefault = isDefault;
        this.owner = owner;
    }

    public boolean isDefault() {
        return isDefault;
    }
    public String getPlaylist_name() {
        return playlist_name;
    }

    public String getPlaylist_id() {
        return playlist_id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
