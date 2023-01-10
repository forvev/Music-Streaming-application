package com.example.musicfun.viewmodel.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.PlaylistRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlaylistViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Playlist>> m_playlist;
    private MutableLiveData<ArrayList<Playlist>> m_searchResult;
    private ArrayList<Playlist> playlist;
    private ArrayList<Playlist> searchResult;
    private Application application;
    private PlaylistRepository db;
    private SharedPreferences sp;

    private String token;

    public PlaylistViewModel(Application application) {
        super(application);
        m_playlist = new MutableLiveData<>();
        m_searchResult = new MutableLiveData<>();

        this.application = application;
        db = new PlaylistRepository(application.getApplicationContext());
        playlist = new ArrayList<>();
        m_playlist.setValue(playlist);

        searchResult = new ArrayList<>();
        m_searchResult.setValue(searchResult);

        sp = getApplication().getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        token = sp.getString("token", "");
    }
    public MutableLiveData<ArrayList<Playlist>> getM_playlist(){
        return m_playlist;
    }

    public MutableLiveData<ArrayList<Playlist>> getM_searchResult(){
        return m_searchResult;
    }

    public void getAllPlaylists() {
        playlist.clear();
        db.getAllPlaylists(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray playlistObject = (JSONArray) response.get("playlists");
                    for (int i = 0; i < playlistObject.length(); i++) {
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"),true, false);
                        playlist.add(p);
                    }
                    getDefaultPlaylist();
                    m_playlist.setValue(playlist);
                    m_searchResult.setValue(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    public void getSharedPlaylists() {
        playlist.clear();
        db.getSharedPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String currentUserId = response.getString("userId");
                    JSONArray playlistObject = (JSONArray) response.get("playlists");
                    for (int i = 0; i < playlistObject.length(); i++) {
                        String owner = playlistObject.getJSONObject(i).getString("owningUser");
                        boolean isOwner = currentUserId.equals(owner);
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"), isOwner, false);
                        playlist.add(p);
                    }
                    getDefaultPlaylist();
                    m_playlist.setValue(playlist);
                    m_searchResult.setValue(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }


    public void getDefaultPlaylist(){
        db.getDefaultPlaylist(new ServerCallBack() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String temp = result.getString("defaultPlaylist");
                    sp.edit().putString("defaultPlaylist", temp);
                    for (int i = 0; i < playlist.size(); i++){
                        Playlist p = playlist.get(i);
                        if (p.getPlaylist_id().equals(temp)){
                            p.setDefault(true);
                        }
                    }
                    m_playlist.setValue(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    public void createPlaylists(String name){
        db.createPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject playlist_info = response.getJSONObject("playlist");
                    playlist.add(new Playlist(name, playlist_info.getString("_id"),true, false));
                    m_playlist.setValue(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, name, token);
    }

    public void createSharedPlaylist(String name){
        db.createSharedPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject playlist_info = response.getJSONObject("playlist");
                    playlist.add(new Playlist(name, playlist_info.getString("_id"),true, false));
                    m_playlist.setValue(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, name, token);
    }

    public void renamePlaylist(String name, int position){
        db.renamePlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                playlist.get(position).setPlaylist_name(name);
                m_playlist.setValue(playlist);
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, playlist.get(position).getPlaylist_id(), name, token);
    }

    public void setAsDefault (int position){
        db.setAsDefault(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, playlist.get(position).getPlaylist_id(), token);

    }

    public void deletePlaylist (int position){
        String id = playlist.get(position).getPlaylist_id();
        db.deletePlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try{
                    if (response.getBoolean("isDefault")){
                        if(position == 0){
                            playlist.get(1).setDefault(true);
                        }
                        else{
                            playlist.get(0).setDefault(true);
                        }
                    }
                    playlist.remove(position);
                    m_playlist.setValue(playlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, id, token);
    }

    public void searchPlaylistByName(String letter){
        searchResult.clear();
        db.searchPlaylistByName(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray playlistObject = (JSONArray) response.get("playlists");
                    for (int i = 0; i < playlistObject.length(); i++) {
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"),true, false);
                        searchResult.add(p);
                    }
                    m_searchResult.setValue(searchResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, letter, token);
    }

}