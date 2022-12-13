package com.example.musicfun.interfaces;

public interface PlaylistMenuClick {
    void renamePlaylist(String playlistName, int position);
    void setDefaultPlaylist(int position, boolean isDefault);
    void deletePlaylist(int position);
    void share(int position);
}
