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

    MutableLiveData<ArrayList<Songs>> songNames;
    private ArrayList<Songs> songsArrayList;
    Application application;
    Database db;
    private String token;

    public MainActivityViewModel(Application application) {
        super(application);
        songNames = new MutableLiveData<>();
        this.application = application;
        db = new Database(application.getApplicationContext());
        songsArrayList = new ArrayList<>();
        songNames.setValue(songsArrayList);
        SharedPreferences sp = application.getSharedPreferences("login",MODE_PRIVATE);
        token = sp.getString("token", "");
    }

    public MutableLiveData<ArrayList<Songs>> getSongNames(){
        return songNames;
    }

    public void init(String url) {
        songsArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songTitles = (JSONArray) response.get("Songs");
                    for (int i = 0; i < songTitles.length(); i++) {
                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getString("_id"));
                        songsArrayList.add(s);
                    }
                    songNames.setValue(songsArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, url);
    }

    public void sendListenHistory(String songId){
        db.sendListenHistory(songId, token);

    }
}
