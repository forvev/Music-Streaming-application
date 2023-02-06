package com.example.musicfun.fragment.mymusic;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class MyMusicFragmentDirections {
  private MyMusicFragmentDirections() {
  }

  @NonNull
  public static ActionMyMusicToMyPlaylistFragment actionMyMusicToMyPlaylistFragment(
      @NonNull String selectedPlaylistId) {
    return new ActionMyMusicToMyPlaylistFragment(selectedPlaylistId);
  }

  public static class ActionMyMusicToMyPlaylistFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionMyMusicToMyPlaylistFragment(@NonNull String selectedPlaylistId) {
      if (selectedPlaylistId == null) {
        throw new IllegalArgumentException("Argument \"selected_playlist_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_playlist_id", selectedPlaylistId);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionMyMusicToMyPlaylistFragment setSelectedPlaylistId(
        @NonNull String selectedPlaylistId) {
      if (selectedPlaylistId == null) {
        throw new IllegalArgumentException("Argument \"selected_playlist_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_playlist_id", selectedPlaylistId);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("selected_playlist_id")) {
        String selectedPlaylistId = (String) arguments.get("selected_playlist_id");
        __result.putString("selected_playlist_id", selectedPlaylistId);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_my_music_to_myPlaylistFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getSelectedPlaylistId() {
      return (String) arguments.get("selected_playlist_id");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionMyMusicToMyPlaylistFragment that = (ActionMyMusicToMyPlaylistFragment) object;
      if (arguments.containsKey("selected_playlist_id") != that.arguments.containsKey("selected_playlist_id")) {
        return false;
      }
      if (getSelectedPlaylistId() != null ? !getSelectedPlaylistId().equals(that.getSelectedPlaylistId()) : that.getSelectedPlaylistId() != null) {
        return false;
      }
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getSelectedPlaylistId() != null ? getSelectedPlaylistId().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionMyMusicToMyPlaylistFragment(actionId=" + getActionId() + "){"
          + "selectedPlaylistId=" + getSelectedPlaylistId()
          + "}";
    }
  }
}
