package com.example.musicfun.viewmodel.discovery;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.repository.Database;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.repository.PlaylistRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscoveryViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<Songs>> songNames;
    private ArrayList<Songs> songsArrayList;
    Application application;
    Database db;
    PlaylistRepository playlistRepository;
    private String token;

    public DiscoveryViewModel(Application application) throws JSONException{
        super(application);
        songNames = new MutableLiveData<>();
        this.application = application;
        db = new Database(application.getApplicationContext());
        playlistRepository = new PlaylistRepository(application.getApplicationContext());
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
                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getInt("_id"));
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

    public void filter(String name){
        songsArrayList.clear();
        db.search(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songTitles = (JSONArray) response.get("Songs");
                    for (int i = 0; i < songTitles.length(); i++) {
                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getInt("_id"));
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
        }, name);
    }

    public void getDefaultPlaylist(int position){
        playlistRepository.getDefaultPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String defaultPlaylist = result.getString("defaultPlaylist");
                    addSongToPlaylist(defaultPlaylist, position);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    public void addSongToPlaylist(String playlist_position, int songlist_position){
        playlistRepository.addSongToPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {


            }
            @Override
            public void onError(VolleyError error) {
                if (error.networkResponse.statusCode == 422){
                    Toast.makeText(application.getApplicationContext(), "This song was already added to your playlist", Toast.LENGTH_SHORT).show();
                }
            }
        }, playlist_position, Integer.toString(songlist_position), token);
    }


}