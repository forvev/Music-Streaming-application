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

public class ActiveListenerFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private ActiveListenerFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private ActiveListenerFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ActiveListenerFragmentArgs fromBundle(@NonNull Bundle bundle) {
    ActiveListenerFragmentArgs __result = new ActiveListenerFragmentArgs();
    bundle.setClassLoader(ActiveListenerFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("usernames")) {
      String usernames;
      usernames = bundle.getString("usernames");
      if (usernames == null) {
        throw new IllegalArgumentException("Argument \"usernames\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("usernames", usernames);
    } else {
      throw new IllegalArgumentException("Required argument \"usernames\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static ActiveListenerFragmentArgs fromSavedStateHandle(
      @NonNull SavedStateHandle savedStateHandle) {
    ActiveListenerFragmentArgs __result = new ActiveListenerFragmentArgs();
    if (savedStateHandle.contains("usernames")) {
      String usernames;
      usernames = savedStateHandle.get("usernames");
      if (usernames == null) {
        throw new IllegalArgumentException("Argument \"usernames\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("usernames", usernames);
    } else {
      throw new IllegalArgumentException("Required argument \"usernames\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getUsernames() {
    return (String) arguments.get("usernames");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("usernames")) {
      String usernames = (String) arguments.get("usernames");
      __result.putString("usernames", usernames);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("usernames")) {
      String usernames = (String) arguments.get("usernames");
      __result.set("usernames", usernames);
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
    ActiveListenerFragmentArgs that = (ActiveListenerFragmentArgs) object;
    if (arguments.containsKey("usernames") != that.arguments.containsKey("usernames")) {
      return false;
    }
    if (getUsernames() != null ? !getUsernames().equals(that.getUsernames()) : that.getUsernames() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getUsernames() != null ? getUsernames().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ActiveListenerFragmentArgs{"
        + "usernames=" + getUsernames()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull ActiveListenerFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(@NonNull String usernames) {
      if (usernames == null) {
        throw new IllegalArgumentException("Argument \"usernames\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("usernames", usernames);
    }

    @NonNull
    public ActiveListenerFragmentArgs build() {
      ActiveListenerFragmentArgs result = new ActiveListenerFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setUsernames(@NonNull String usernames) {
      if (usernames == null) {
        throw new IllegalArgumentException("Argument \"usernames\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("usernames", usernames);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getUsernames() {
      return (String) arguments.get("usernames");
    }
  }
}
