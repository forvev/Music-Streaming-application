package com.example.musicfun.fragment.sharedplaylist;

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

public class SharedPlaylistSongsFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private SharedPlaylistSongsFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private SharedPlaylistSongsFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static SharedPlaylistSongsFragmentArgs fromBundle(@NonNull Bundle bundle) {
    SharedPlaylistSongsFragmentArgs __result = new SharedPlaylistSongsFragmentArgs();
    bundle.setClassLoader(SharedPlaylistSongsFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("selected_shared_id")) {
      String selectedSharedId;
      selectedSharedId = bundle.getString("selected_shared_id");
      if (selectedSharedId == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_shared_id", selectedSharedId);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_shared_id\" is missing and does not have an android:defaultValue");
    }
    if (bundle.containsKey("isOwner")) {
      boolean isOwner;
      isOwner = bundle.getBoolean("isOwner");
      __result.arguments.put("isOwner", isOwner);
    } else {
      throw new IllegalArgumentException("Required argument \"isOwner\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static SharedPlaylistSongsFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    SharedPlaylistSongsFragmentArgs __result = new SharedPlaylistSongsFragmentArgs();
    if (savedStateHandle.contains("selected_shared_id")) {
      String selectedSharedId;
      selectedSharedId = savedStateHandle.get("selected_shared_id");
      if (selectedSharedId == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_shared_id", selectedSharedId);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_shared_id\" is missing and does not have an android:defaultValue");
    }
    if (savedStateHandle.contains("isOwner")) {
      boolean isOwner;
      isOwner = savedStateHandle.get("isOwner");
      __result.arguments.put("isOwner", isOwner);
    } else {
      throw new IllegalArgumentException("Required argument \"isOwner\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getSelectedSharedId() {
    return (String) arguments.get("selected_shared_id");
  }

  @SuppressWarnings("unchecked")
  public boolean getIsOwner() {
    return (boolean) arguments.get("isOwner");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("selected_shared_id")) {
      String selectedSharedId = (String) arguments.get("selected_shared_id");
      __result.putString("selected_shared_id", selectedSharedId);
    }
    if (arguments.containsKey("isOwner")) {
      boolean isOwner = (boolean) arguments.get("isOwner");
      __result.putBoolean("isOwner", isOwner);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("selected_shared_id")) {
      String selectedSharedId = (String) arguments.get("selected_shared_id");
      __result.set("selected_shared_id", selectedSharedId);
    }
    if (arguments.containsKey("isOwner")) {
      boolean isOwner = (boolean) arguments.get("isOwner");
      __result.set("isOwner", isOwner);
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
    SharedPlaylistSongsFragmentArgs that = (SharedPlaylistSongsFragmentArgs) object;
    if (arguments.containsKey("selected_shared_id") != that.arguments.containsKey("selected_shared_id")) {
      return false;
    }
    if (getSelectedSharedId() != null ? !getSelectedSharedId().equals(that.getSelectedSharedId()) : that.getSelectedSharedId() != null) {
      return false;
    }
    if (arguments.containsKey("isOwner") != that.arguments.containsKey("isOwner")) {
      return false;
    }
    if (getIsOwner() != that.getIsOwner()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getSelectedSharedId() != null ? getSelectedSharedId().hashCode() : 0);
    result = 31 * result + (getIsOwner() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SharedPlaylistSongsFragmentArgs{"
        + "selectedSharedId=" + getSelectedSharedId()
        + ", isOwner=" + getIsOwner()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull SharedPlaylistSongsFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String selectedSharedId, boolean isOwner) {
      if (selectedSharedId == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id", selectedSharedId);
      this.arguments.put("isOwner", isOwner);
    }

    @NonNull
    public SharedPlaylistSongsFragmentArgs build() {
      SharedPlaylistSongsFragmentArgs result = new SharedPlaylistSongsFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setSelectedSharedId(@NonNull String selectedSharedId) {
      if (selectedSharedId == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id", selectedSharedId);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setIsOwner(boolean isOwner) {
      this.arguments.put("isOwner", isOwner);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getSelectedSharedId() {
      return (String) arguments.get("selected_shared_id");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public boolean getIsOwner() {
      return (boolean) arguments.get("isOwner");
    }
  }
}
