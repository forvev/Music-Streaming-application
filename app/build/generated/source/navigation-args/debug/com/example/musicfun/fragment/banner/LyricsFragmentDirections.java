package com.example.musicfun.fragment.banner;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class LyricsFragmentDirections {
  private LyricsFragmentDirections() {
  }

  @NonNull
  public static ActionLyricsFragmentToCurrentPlaylistFragment actionLyricsFragmentToCurrentPlaylistFragment(
      @NonNull String playlist) {
    return new ActionLyricsFragmentToCurrentPlaylistFragment(playlist);
  }

  @NonNull
  public static ActionLyricsFragmentToActiveListenerFragment actionLyricsFragmentToActiveListenerFragment(
      @NonNull String usernames) {
    return new ActionLyricsFragmentToActiveListenerFragment(usernames);
  }

  @NonNull
  public static NavDirections actionLyricsFragmentToChoosePlaylistFragment2() {
    return new ActionOnlyNavDirections(R.id.action_lyricsFragment_to_choosePlaylistFragment2);
  }

  public static class ActionLyricsFragmentToCurrentPlaylistFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionLyricsFragmentToCurrentPlaylistFragment(@NonNull String playlist) {
      if (playlist == null) {
        throw new IllegalArgumentException("Argument \"playlist\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("playlist", playlist);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionLyricsFragmentToCurrentPlaylistFragment setPlaylist(@NonNull String playlist) {
      if (playlist == null) {
        throw new IllegalArgumentException("Argument \"playlist\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("playlist", playlist);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("playlist")) {
        String playlist = (String) arguments.get("playlist");
        __result.putString("playlist", playlist);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_lyricsFragment_to_currentPlaylistFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getPlaylist() {
      return (String) arguments.get("playlist");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionLyricsFragmentToCurrentPlaylistFragment that = (ActionLyricsFragmentToCurrentPlaylistFragment) object;
      if (arguments.containsKey("playlist") != that.arguments.containsKey("playlist")) {
        return false;
      }
      if (getPlaylist() != null ? !getPlaylist().equals(that.getPlaylist()) : that.getPlaylist() != null) {
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
      result = 31 * result + (getPlaylist() != null ? getPlaylist().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionLyricsFragmentToCurrentPlaylistFragment(actionId=" + getActionId() + "){"
          + "playlist=" + getPlaylist()
          + "}";
    }
  }

  public static class ActionLyricsFragmentToActiveListenerFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionLyricsFragmentToActiveListenerFragment(@NonNull String usernames) {
      if (usernames == null) {
        throw new IllegalArgumentException("Argument \"usernames\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("usernames", usernames);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionLyricsFragmentToActiveListenerFragment setUsernames(@NonNull String usernames) {
      if (usernames == null) {
        throw new IllegalArgumentException("Argument \"usernames\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("usernames", usernames);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("usernames")) {
        String usernames = (String) arguments.get("usernames");
        __result.putString("usernames", usernames);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_lyricsFragment_to_activeListenerFragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getUsernames() {
      return (String) arguments.get("usernames");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionLyricsFragmentToActiveListenerFragment that = (ActionLyricsFragmentToActiveListenerFragment) object;
      if (arguments.containsKey("usernames") != that.arguments.containsKey("usernames")) {
        return false;
      }
      if (getUsernames() != null ? !getUsernames().equals(that.getUsernames()) : that.getUsernames() != null) {
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
      result = 31 * result + (getUsernames() != null ? getUsernames().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionLyricsFragmentToActiveListenerFragment(actionId=" + getActionId() + "){"
          + "usernames=" + getUsernames()
          + "}";
    }
  }
}
