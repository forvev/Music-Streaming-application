package com.example.musicfun.DiscoveryPart;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.musicfun.repository.Database;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.datatype.Songs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscoveryMostHeardViewModel  extends AndroidViewModel {
    MutableLiveData<ArrayList<Songs>> songNames;
    private ArrayList<Songs> songsArrayList;
    Application application;
    Database db;

    public DiscoveryMostHeardViewModel(Application application) throws JSONException {
        super(application);
        songNames = new MutableLiveData<>();
        this.application = application;
        db = new Database(application.getApplicationContext());
        songsArrayList = new ArrayList<>();
        songNames.setValue(songsArrayList);
    }
    public MutableLiveData<ArrayList<Songs>> getSongNames(){
        return songNames;
    }

    public void init() {
        songsArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songTitles = (JSONArray) response.get("Songs");
                    for (int i = 0; i < songTitles.length(); i++) {
                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getInt("id"));
                        songsArrayList.add(s);
                    }
                    songNames.setValue(songsArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "get/popularSongsAmongFriends");
    }
}
