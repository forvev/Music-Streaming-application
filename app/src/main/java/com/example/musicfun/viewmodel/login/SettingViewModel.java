package com.example.musicfun.viewmodel.login;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.musicfun.repository.LoginRepository;

/**
 * This class serves as an intermediate station for setting related database accesses.
 * It is used in the SettingFragment and is responsible for the status storage on the server in case of a logout.
 */
public class SettingViewModel extends AndroidViewModel {

    Application application;
    SharedPreferences sp;
    LoginRepository loginRepository;


    public SettingViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        loginRepository = new LoginRepository(application.getApplicationContext());
        sp = getApplication().getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
    }

    public void saveDataWhenLogout(int startItemIndex, long startPosition, String savedPlaylist){
        String toLong = Long.toString(startPosition);
        loginRepository.saveDataWhenLogout(startItemIndex, toLong, savedPlaylist, sp.getString("token", ""));
    }
}
