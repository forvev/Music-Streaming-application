package com.example.musicfun.interfaces;

import com.example.musicfun.datatype.Playlist;

import java.util.ArrayList;
import java.util.List;

public interface PlaylistMenuClick {
    void renamePlaylist(String playlistName, String playlist_id);
    void setDefaultPlaylist(String playlist_id, boolean isDefault);
    void deletePlaylist(String playlist_id);
    void share(List<Playlist> my_playlists, String playlist_id);
}
