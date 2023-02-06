package com.example.musicfun.fragment.sharedplaylist;

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

public class SharedPlaylistSongsFragmentDirections {
  private SharedPlaylistSongsFragmentDirections() {
  }

  @NonNull
  public static ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3 actionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3(
      @NonNull String selectedSharedId3, boolean isOwner) {
    return new ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3(selectedSharedId3, isOwner);
  }

  @NonNull
  public static NavDirections actionSharedPlaylistSongsFragmentToChoosePlaylistFragment3() {
    return new ActionOnlyNavDirections(R.id.action_sharedPlaylistSongsFragment_to_choosePlaylistFragment3);
  }

  public static class ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3 implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3(
        @NonNull String selectedSharedId3, boolean isOwner) {
      if (selectedSharedId3 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_3\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_3", selectedSharedId3);
      this.arguments.put("isOwner", isOwner);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3 setSelectedSharedId3(
        @NonNull String selectedSharedId3) {
      if (selectedSharedId3 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_3\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_3", selectedSharedId3);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3 setIsOwner(
        boolean isOwner) {
      this.arguments.put("isOwner", isOwner);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
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

    @Override
    public int getActionId() {
      return R.id.action_sharedPlaylistSongsFragment_to_sharedPlaylistParticipants3;
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

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3 that = (ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3) object;
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
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getSelectedSharedId3() != null ? getSelectedSharedId3().hashCode() : 0);
      result = 31 * result + (getIsOwner() ? 1 : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionSharedPlaylistSongsFragmentToSharedPlaylistParticipants3(actionId=" + getActionId() + "){"
          + "selectedSharedId3=" + getSelectedSharedId3()
          + ", isOwner=" + getIsOwner()
          + "}";
    }
  }
}
