package com.example.musicfun.fragment.discovery;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;

public class NewReleaseFragmentDirections {
  private NewReleaseFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionNewReleaseFragmentToChoosePlaylistFragment() {
    return new ActionOnlyNavDirections(R.id.action_newReleaseFragment_to_choosePlaylistFragment);
  }
}
