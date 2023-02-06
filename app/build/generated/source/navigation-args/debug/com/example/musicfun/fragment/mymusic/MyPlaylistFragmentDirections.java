package com.example.musicfun.fragment.mymusic;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;

public class MyPlaylistFragmentDirections {
  private MyPlaylistFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionMyPlaylistFragmentToChooseOnePlaylist() {
    return new ActionOnlyNavDirections(R.id.action_myPlaylistFragment_to_choose_one_playlist);
  }
}
