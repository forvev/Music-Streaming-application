package com.example.musicfun.fragment.friends;

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

public class List_of_friends_fragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private List_of_friends_fragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private List_of_friends_fragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static List_of_friends_fragmentArgs fromBundle(@NonNull Bundle bundle) {
    List_of_friends_fragmentArgs __result = new List_of_friends_fragmentArgs();
    bundle.setClassLoader(List_of_friends_fragmentArgs.class.getClassLoader());
    if (bundle.containsKey("selected_shared_id_2")) {
      String selectedSharedId2;
      selectedSharedId2 = bundle.getString("selected_shared_id_2");
      if (selectedSharedId2 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_2\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_shared_id_2", selectedSharedId2);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_shared_id_2\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static List_of_friends_fragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    List_of_friends_fragmentArgs __result = new List_of_friends_fragmentArgs();
    if (savedStateHandle.contains("selected_shared_id_2")) {
      String selectedSharedId2;
      selectedSharedId2 = savedStateHandle.get("selected_shared_id_2");
      if (selectedSharedId2 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_2\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("selected_shared_id_2", selectedSharedId2);
    } else {
      throw new IllegalArgumentException("Required argument \"selected_shared_id_2\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getSelectedSharedId2() {
    return (String) arguments.get("selected_shared_id_2");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("selected_shared_id_2")) {
      String selectedSharedId2 = (String) arguments.get("selected_shared_id_2");
      __result.putString("selected_shared_id_2", selectedSharedId2);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("selected_shared_id_2")) {
      String selectedSharedId2 = (String) arguments.get("selected_shared_id_2");
      __result.set("selected_shared_id_2", selectedSharedId2);
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
    List_of_friends_fragmentArgs that = (List_of_friends_fragmentArgs) object;
    if (arguments.containsKey("selected_shared_id_2") != that.arguments.containsKey("selected_shared_id_2")) {
      return false;
    }
    if (getSelectedSharedId2() != null ? !getSelectedSharedId2().equals(that.getSelectedSharedId2()) : that.getSelectedSharedId2() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getSelectedSharedId2() != null ? getSelectedSharedId2().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "List_of_friends_fragmentArgs{"
        + "selectedSharedId2=" + getSelectedSharedId2()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull List_of_friends_fragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String selectedSharedId2) {
      if (selectedSharedId2 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_2\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_2", selectedSharedId2);
    }

    @NonNull
    public List_of_friends_fragmentArgs build() {
      List_of_friends_fragmentArgs result = new List_of_friends_fragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setSelectedSharedId2(@NonNull String selectedSharedId2) {
      if (selectedSharedId2 == null) {
        throw new IllegalArgumentException("Argument \"selected_shared_id_2\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("selected_shared_id_2", selectedSharedId2);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getSelectedSharedId2() {
      return (String) arguments.get("selected_shared_id_2");
    }
  }
}
