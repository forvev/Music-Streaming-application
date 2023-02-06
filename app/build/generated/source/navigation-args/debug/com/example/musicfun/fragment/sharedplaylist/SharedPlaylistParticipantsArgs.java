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

public class SharedPlaylistParticipantsArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private SharedPlaylistParticipantsArgs() {
  }

  @SuppressWarnings("unchecked")
  private SharedPlaylistParticipantsArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static SharedPlaylistParticipantsArgs fromBundle(@NonNull Bundle bundle) {
    SharedPlaylistParticipantsArgs __result = new SharedPlaylistParticipantsArgs();
    bundle.setClassLoader(SharedPlaylistParticipantsArgs.class.getClassLoader());
    if (bundle.containsKey("selected_shared_id_3")) {
      String selectedSharedId3;
      selectedSharedId3 = bundle.getString("selected_shared_id_3");
      if (selectedSharedId3 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_3\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_shared_id_3", selectedSharedId3);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_shared_id_3\" is missing and does not have an android:defaultValue");
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
  public static SharedPlaylistParticipantsArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    SharedPlaylistParticipantsArgs __result = new SharedPlaylistParticipantsArgs();
    if (savedStateHandle.contains("selected_shared_id_3")) {
      String selectedSharedId3;
      selectedSharedId3 = savedStateHandle.get("selected_shared_id_3");
      if (selectedSharedId3 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_3\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_shared_id_3", selectedSharedId3);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_shared_id_3\" is missing and does not have an android:defaultValue");
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
  public String getSelectedSharedId3() {
    return (String) arguments.get("selected_shared_id_3");
  }

  @SuppressWarnings("unchecked")
  public boolean getIsOwner() {
    return (boolean) arguments.get("isOwner");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("selected_shared_id_3")) {
      String selectedSharedId3 = (String) arguments.get("selected_shared_id_3");
      __result.putString("selected_shared_id_3", selectedSharedId3);
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
    if (arguments.containsKey("selected_shared_id_3")) {
      String selectedSharedId3 = (String) arguments.get("selected_shared_id_3");
      __result.set("selected_shared_id_3", selectedSharedId3);
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
    SharedPlaylistParticipantsArgs that = (SharedPlaylistParticipantsArgs) object;
    if (arguments.containsKey("selected_shared_id_3") != that.arguments.containsKey("selected_shared_id_3")) {
      return false;
    }
    if (getSelectedSharedId3() != null ? !getSelectedSharedId3().equals(that.getSelectedSharedId3()) : that.getSelectedSharedId3() != null) {
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
    result = 31 * result + (getSelectedSharedId3() != null ? getSelectedSharedId3().hashCode() : 0);
    result = 31 * result + (getIsOwner() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "SharedPlaylistParticipantsArgs{"
        + "selectedSharedId3=" + getSelectedSharedId3()
        + ", isOwner=" + getIsOwner()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull SharedPlaylistParticipantsArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String selectedSharedId3, boolean isOwner) {
      if (selectedSharedId3 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_3\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_3", selectedSharedId3);
      this.arguments.put("isOwner", isOwner);
    }

    @NonNull
    public SharedPlaylistParticipantsArgs build() {
      SharedPlaylistParticipantsArgs result = new SharedPlaylistParticipantsArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setSelectedSharedId3(@NonNull String selectedSharedId3) {
      if (selectedSharedId3 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_3\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_3", selectedSharedId3);
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
    public String getSelectedSharedId3() {
      return (String) arguments.get("selected_shared_id_3");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public boolean getIsOwner() {
      return (boolean) arguments.get("isOwner");
    }
  }
}
