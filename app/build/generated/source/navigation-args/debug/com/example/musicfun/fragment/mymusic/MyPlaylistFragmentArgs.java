package com.example.musicfun.fragment.mymusic;

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

public class MyPlaylistFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private MyPlaylistFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private MyPlaylistFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static MyPlaylistFragmentArgs fromBundle(@NonNull Bundle bundle) {
    MyPlaylistFragmentArgs __result = new MyPlaylistFragmentArgs();
    bundle.setClassLoader(MyPlaylistFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("selected_playlist_id")) {
      String selectedPlaylistId;
      selectedPlaylistId = bundle.getString("selected_playlist_id");
      if (selectedPlaylistId == null) {
        throw new IllegalArgumentException("Argument \"selected_playlist_id\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_playlist_id", selectedPlaylistId);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_playlist_id\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static MyPlaylistFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    MyPlaylistFragmentArgs __result = new MyPlaylistFragmentArgs();
    if (savedStateHandle.contains("selected_playlist_id")) {
      String selectedPlaylistId;
      selectedPlaylistId = savedStateHandle.get("selected_playlist_id");
      if (selectedPlaylistId == null) {
        throw new IllegalArgumentException("Argument \"selected_playlist_id\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_playlist_id", selectedPlaylistId);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_playlist_id\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getSelectedPlaylistId() {
    return (String) arguments.get("selected_playlist_id");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("selected_playlist_id")) {
      String selectedPlaylistId = (String) arguments.get("selected_playlist_id");
      __result.putString("selected_playlist_id", selectedPlaylistId);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("selected_playlist_id")) {
      String selectedPlaylistId = (String) arguments.get("selected_playlist_id");
      __result.set("selected_playlist_id", selectedPlaylistId);
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
    MyPlaylistFragmentArgs that = (MyPlaylistFragmentArgs) object;
    if (arguments.containsKey("selected_playlist_id") != that.arguments.containsKey("selected_playlist_id")) {
      return false;
    }
    if (getSelectedPlaylistId() != null ? !getSelectedPlaylistId().equals(that.getSelectedPlaylistId()) : that.getSelectedPlaylistId() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getSelectedPlaylistId() != null ? getSelectedPlaylistId().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "MyPlaylistFragmentArgs{"
        + "selectedPlaylistId=" + getSelectedPlaylistId()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull MyPlaylistFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String selectedPlaylistId) {
      if (selectedPlaylistId == null) {
        throw new IllegalArgumentException("Argument \"selected_playlist_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_playlist_id", selectedPlaylistId);
    }

    @NonNull
    public MyPlaylistFragmentArgs build() {
      MyPlaylistFragmentArgs result = new MyPlaylistFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setSelectedPlaylistId(@NonNull String selectedPlaylistId) {
      if (selectedPlaylistId == null) {
        throw new IllegalArgumentException("Argument \"selected_playlist_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_playlist_id", selectedPlaylistId);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getSelectedPlaylistId() {
      return (String) arguments.get("selected_playlist_id");
    }
  }
}
