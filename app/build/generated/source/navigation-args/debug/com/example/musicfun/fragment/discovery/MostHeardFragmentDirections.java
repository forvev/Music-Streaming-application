package com.example.musicfun.fragment.discovery;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;

public class MostHeardFragmentDirections {
  private MostHeardFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionMostHeardFragmentToChoosePlaylistFragment() {
    return new ActionOnlyNavDirections(R.id.action_mostHeardFragment_to_choosePlaylistFragment);
  }
}
