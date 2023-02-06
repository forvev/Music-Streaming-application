package com.example.musicfun.fragment.login;

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

public class GenreFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private GenreFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private GenreFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static GenreFragmentArgs fromBundle(@NonNull Bundle bundle) {
    GenreFragmentArgs __result = new GenreFragmentArgs();
    bundle.setClassLoader(GenreFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("fromSetting")) {
      boolean fromSetting;
      fromSetting = bundle.getBoolean("fromSetting");
      __result.arguments.put("fromSetting", fromSetting);
    } else {
      throw new IllegalArgumentException("Required argument \"fromSetting\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static GenreFragmentArgs fromSavedStateHandle(@NonNull SavedStateHandle savedStateHandle) {
    GenreFragmentArgs __result = new GenreFragmentArgs();
    if (savedStateHandle.contains("fromSetting")) {
      boolean fromSetting;
      fromSetting = savedStateHandle.get("fromSetting");
      __result.arguments.put("fromSetting", fromSetting);
    } else {
      throw new IllegalArgumentException("Required argument \"fromSetting\" is missing and does not have an android:defaultValue");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  public boolean getFromSetting() {
    return (boolean) arguments.get("fromSetting");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("fromSetting")) {
      boolean fromSetting = (boolean) arguments.get("fromSetting");
      __result.putBoolean("fromSetting", fromSetting);
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("fromSetting")) {
      boolean fromSetting = (boolean) arguments.get("fromSetting");
      __result.set("fromSetting", fromSetting);
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
    GenreFragmentArgs that = (GenreFragmentArgs) object;
    if (arguments.containsKey("fromSetting") != that.arguments.containsKey("fromSetting")) {
      return false;
    }
    if (getFromSetting() != that.getFromSetting()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getFromSetting() ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "GenreFragmentArgs{"
        + "fromSetting=" + getFromSetting()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull GenreFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    @SuppressWarnings("unchecked")
    public Builder(boolean fromSetting) {
      this.arguments.put("fromSetting", fromSetting);
    }

    @NonNull
    public GenreFragmentArgs build() {
      GenreFragmentArgs result = new GenreFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setFromSetting(boolean fromSetting) {
      this.arguments.put("fromSetting", fromSetting);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public boolean getFromSetting() {
      return (boolean) arguments.get("fromSetting");
    }
  }
}
