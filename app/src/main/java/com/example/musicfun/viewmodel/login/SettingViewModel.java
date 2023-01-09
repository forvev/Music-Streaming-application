package com.example.musicfun.viewmodel.login;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.musicfun.repository.LoginRepository;

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
