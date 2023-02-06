package com.example.musicfun.fragment.login;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import com.example.musicfun.R;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class RegisterFragmentDirections {
  private RegisterFragmentDirections() {
  }

  @NonNull
  public static NavDirections actionRegisterFragmentToLoginFragment() {
    return new ActionOnlyNavDirections(R.id.action_registerFragment_to_loginFragment);
  }

  @NonNull
  public static ActionRegisterFragmentToGenreFragment actionRegisterFragmentToGenreFragment(
      boolean fromSetting) {
    return new ActionRegisterFragmentToGenreFragment(fromSetting);
  }

  public static class ActionRegisterFragmentToGenreFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    private ActionRegisterFragmentToGenreFragment(boolean fromSetting) {
      this.arguments.put("fromSetting", fromSetting);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionRegisterFragmentToGenreFragment setFromSetting(boolean fromSetting) {
      this.arguments.put("fromSetting", fromSetting);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
      Bundle __result = new Bundle();
      if (arguments.containsKey("fromSetting")) {
        boolean fromSetting = (boolean) arguments.get("fromSetting");
        __result.putBoolean("fromSetting", fromSetting);
      }
      return __result;
    }

    @Override
    public int getActionId() {
      return R.id.action_registerFragment_to_genreFragment;
    }

    @SuppressWarnings("unchecked")
    public boolean getFromSetting() {
      return (boolean) arguments.get("fromSetting");
    }

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionRegisterFragmentToGenreFragment that = (ActionRegisterFragmentToGenreFragment) object;
      if (arguments.containsKey("fromSetting") != that.arguments.containsKey("fromSetting")) {
        return false;
      }
      if (getFromSetting() != that.getFromSetting()) {
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
      result = 31 * result + (getFromSetting() ? 1 : 0);
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionRegisterFragmentToGenreFragment(actionId=" + getActionId() + "){"
          + "fromSetting=" + getFromSetting()
          + "}";
    }
  }
}
