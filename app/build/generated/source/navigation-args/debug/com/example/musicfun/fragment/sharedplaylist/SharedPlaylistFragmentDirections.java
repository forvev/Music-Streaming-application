package com.example.musicfun.fragment.sharedplaylist;

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

public class SharedPlaylistFragmentDirections {
  private SharedPlaylistFragmentDirections() {
  }

  @NonNull
  public static ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment actionSharedPlaylistFragmentToSharedPlaylistSongsFragment(
      @NonNull String selectedSharedId, boolean isOwner) {
    return new ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment(selectedSharedId, isOwner);
  }

  @NonNull
  public static ActionFriendsSharedPlaylistToListOfFriendsFragment actionFriendsSharedPlaylistToListOfFriendsFragment(
      @NonNull String selectedSharedId2) {
    return new ActionFriendsSharedPlaylistToListOfFriendsFragment(selectedSharedId2);
  }

  public static class ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment(
        @NonNull String selectedSharedId, boolean isOwner) {
      if (selectedSharedId == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id", selectedSharedId);
      this.arguments.put("isOwner", isOwner);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment setSelectedSharedId(
        @NonNull String selectedSharedId) {
      if (selectedSharedId == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id", selectedSharedId);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment setIsOwner(boolean isOwner) {
      this.arguments.put("isOwner", isOwner);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
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

    @Override
    public int getActionId() {
      return R.id.action_sharedPlaylistFragment_to_sharedPlaylistSongsFragment;
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

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment that = (ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment) object;
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
      if (getActionId() != that.getActionId()) {
        return false;
      }
      return true;
    }

    @Override
    public int hashCode() {
      int result = 1;
      result = 31 * result + (getSelectedSharedId() != null ? getSelectedSharedId().hashCode() : 0);
      result = 31 * result + (getIsOwner() ? 1 : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionSharedPlaylistFragmentToSharedPlaylistSongsFragment(actionId=" + getActionId() + "){"
          + "selectedSharedId=" + getSelectedSharedId()
          + ", isOwner=" + getIsOwner()
          + "}";
    }
  }

  public static class ActionFriendsSharedPlaylistToListOfFriendsFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionFriendsSharedPlaylistToListOfFriendsFragment(@NonNull String selectedSharedId2) {
      if (selectedSharedId2 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_2\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_2", selectedSharedId2);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionFriendsSharedPlaylistToListOfFriendsFragment setSelectedSharedId2(
        @NonNull String selectedSharedId2) {
      if (selectedSharedId2 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_2\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_2", selectedSharedId2);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("selected_shared_id_2")) {
        String selectedSharedId2 = (String) arguments.get("selected_shared_id_2");
        __result.putString("selected_shared_id_2", selectedSharedId2);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_friends_sharedPlaylist_to_list_of_friends_fragment;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public String getSelectedSharedId2() {
      return (String) arguments.get("selected_shared_id_2");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionFriendsSharedPlaylistToListOfFriendsFragment that = (ActionFriendsSharedPlaylistToListOfFriendsFragment) object;
      if (arguments.containsKey("selected_shared_id_2") != that.arguments.containsKey("selected_shared_id_2")) {
        return false;
      }
      if (getSelectedSharedId2() != null ? !getSelectedSharedId2().equals(that.getSelectedSharedId2()) : that.getSelectedSharedId2() != null) {
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
      result = 31 * result + (getSelectedSharedId2() != null ? getSelectedSharedId2().hashCode() : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionFriendsSharedPlaylistToListOfFriendsFragment(actionId=" + getActionId() + "){"
          + "selectedSharedId2=" + getSelectedSharedId2()
          + "}";
    }
  }
}
