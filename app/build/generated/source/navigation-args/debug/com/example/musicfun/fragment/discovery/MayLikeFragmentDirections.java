package com.example.musicfun.fragment.discovery;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;

public class MayLikeFragmentDirections {
  private MayLikeFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionMayLikeFragmentToChoosePlaylistFragment() {
    return new ActionOnlyNavDirections(R.id.action_mayLikeFragment_to_choosePlaylistFragment);
  }
}
