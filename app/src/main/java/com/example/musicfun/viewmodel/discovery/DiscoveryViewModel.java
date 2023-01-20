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
    MutableLiveData<ArrayList<Songs>> initList;
    private ArrayList<Songs> songsArrayList;
    private ArrayList<Songs> initArrayList;
    Application application;
    Database db;
    PlaylistRepository playlistRepository;
    private String token;

    public DiscoveryViewModel(Application application){
        super(application);
        this.application = application;
        db = new Database(application.getApplicationContext());
        playlistRepository = new PlaylistRepository(application.getApplicationContext());

        songNames = new MutableLiveData<>();
        songsArrayList = new ArrayList<>();
        songNames.setValue(songsArrayList);
//        initArrayList is used for MainActivity, so that the mutablelivedata can response to MainActivity instead of DiscoveryFragments
        initList = new MutableLiveData<>();
        initArrayList = new ArrayList<>();
        initList.setValue(initArrayList);
        SharedPreferences sp = application.getSharedPreferences("login",MODE_PRIVATE);
        token = sp.getString("token", "");
    }

    public MutableLiveData<ArrayList<Songs>> getSongNames(){
        return songNames;
    }

    public MutableLiveData<ArrayList<Songs>> getInitList(){
        return initList;
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

    public void initInMain (String url){
        initArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songTitles = (JSONArray) response.get("Songs");
                    for (int i = 0; i < songTitles.length(); i++) {
                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getString("_id"));
                        initArrayList.add(s);
                    }
                    initList.setValue(initArrayList);
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
        }, name);
    }

    public void getDefaultPlaylist(String position){
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

    public void addSongToPlaylist(String playlist_position, String songlist_position){
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
        }, playlist_position, songlist_position, token);
    }


}