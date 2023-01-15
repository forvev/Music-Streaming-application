package com.example.musicfun.interfaces;

import com.example.musicfun.datatype.Playlist;

import java.util.ArrayList;
import java.util.List;

public interface PlaylistMenuClick {
    void renamePlaylist(String playlistName, int position);
    void setDefaultPlaylist(int position, boolean isDefault);
    void deletePlaylist(int position);
    void share(List<Playlist> my_playlists, int position);
}
