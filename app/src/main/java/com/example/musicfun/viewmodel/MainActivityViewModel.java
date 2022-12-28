package com.example.musicfun.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivityViewModel extends AndroidViewModel {

    Application application;
    Database db;
    private String token;

    public MainActivityViewModel(Application application) {
        super(application);
        this.application = application;
        db = new Database(application.getApplicationContext());
        SharedPreferences sp = application.getSharedPreferences("login",MODE_PRIVATE);
        token = sp.getString("token", "");
    }

    public void sendListenHistory(String songId){
        db.sendListenHistory(songId, token);
    }

}
