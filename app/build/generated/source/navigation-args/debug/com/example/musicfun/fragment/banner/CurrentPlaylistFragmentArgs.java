package com.example.musicfun.fragment.banner;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class CurrentPlaylistFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private CurrentPlaylistFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private CurrentPlaylistFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static CurrentPlaylistFragmentArgs fromBundle(@NonNull Bundle bundle) {
    CurrentPlaylistFragmentArgs __result = new CurrentPlaylistFragmentArgs();
    bundle.setClassLoader(CurrentPlaylistFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("playlist")) {
      String playlist;
      playlist = bundle.getString("playlist");
      if (playlist == null) {
        throw new IllegalArgumentException("Argument \"playlist\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("playlist", playlist);
    } else {
      throw new IllegalArgumentException("Required argument \"playlist\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static CurrentPlaylistFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    CurrentPlaylistFragmentArgs __result = new CurrentPlaylistFragmentArgs();
    if (savedStateHandle.contains("playlist")) {
      String playlist;
      playlist = savedStateHandle.get("playlist");
      if (playlist == null) {
        throw new IllegalArgumentException("Argument \"playlist\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("playlist", playlist);
    } else {
      throw new IllegalArgumentException("Required argument \"playlist\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getPlaylist() {
    return (String) arguments.get("playlist");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("playlist")) {
      String playlist = (String) arguments.get("playlist");
      __result.putString("playlist", playlist);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("playlist")) {
      String playlist = (String) arguments.get("playlist");
      __result.set("playlist", playlist);
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    CurrentPlaylistFragmentArgs that = (CurrentPlaylistFragmentArgs) object;
    if (arguments.containsKey("playlist") != that.arguments.containsKey("playlist")) {
      return false;
    }
    if (getPlaylist() != null ? !getPlaylist().equals(that.getPlaylist()) : that.getPlaylist() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getPlaylist() != null ? getPlaylist().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CurrentPlaylistFragmentArgs{"
        + "playlist=" + getPlaylist()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull CurrentPlaylistFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String playlist) {
      if (playlist == null) {
        throw new IllegalArgumentException("Argument \"playlist\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("playlist", playlist);
    }

    @NonNull
    public CurrentPlaylistFragmentArgs build() {
      CurrentPlaylistFragmentArgs result = new CurrentPlaylistFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setPlaylist(@NonNull String playlist) {
      if (playlist == null) {
        throw new IllegalArgumentException("Argument \"playlist\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("playlist", playlist);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getPlaylist() {
      return (String) arguments.get("playlist");
    }
  }
}
