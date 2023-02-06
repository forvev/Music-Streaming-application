package com.example.musicfun.fragment.discovery;

import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;

public class ChartsFragmentDirections {
  private ChartsFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionChartsFragment2ToChoosePlaylistFragment() {
    return new ActionOnlyNavDirections(R.id.action_chartsFragment2_to_choosePlaylistFragment);
  }
}
