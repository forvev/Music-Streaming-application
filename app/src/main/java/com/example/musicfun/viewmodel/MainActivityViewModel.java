package com.example.musicfun.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Lyrics;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * View model for :
 * 1. fetching the lyrics from the server,
 * 2. Sending listening history
 */
public class MainActivityViewModel extends AndroidViewModel {

    Application application;
    Database db;
    private String token;
    private MutableLiveData<ArrayList<Lyrics>> m_songLrc;
    private ArrayList<Lyrics> songLrc;

    public MainActivityViewModel(Application application) {
        super(application);
        this.application = application;
        db = new Database(application.getApplicationContext());
        SharedPreferences sp = application.getSharedPreferences("login",MODE_PRIVATE);
        token = sp.getString("token", "");

        songLrc = new ArrayList<>();
        m_songLrc = new MutableLiveData<>();
        m_songLrc.setValue(songLrc);
    }

    public MutableLiveData<ArrayList<Lyrics>> getM_songLrc(){
        return m_songLrc;
    }

    public void sendListenHistory(String songId){
        db.sendListenHistory(songId, token);
    }

    public void fetchLyrics (String song_id){
        songLrc.clear();
        db.fetchLyrics(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray lyrics_array = (JSONArray) result.get("Lyrics");
                    for (int i = 0; i < lyrics_array.length(); i++) {
                        Lyrics lrc = new Lyrics(lyrics_array.getJSONObject(i).getInt("timestamp"), lyrics_array.getJSONObject(i).getString("lyrics"));
                        songLrc.add(lrc);
                    }
                    m_songLrc.setValue(songLrc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, song_id);
    }
}
