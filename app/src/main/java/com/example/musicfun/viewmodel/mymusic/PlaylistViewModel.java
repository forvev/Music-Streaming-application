package com.example.musicfun.viewmodel.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.R;
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
    private String username;

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
        username = sp.getString("name", "");
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
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"), username, false, playlistObject.getJSONObject(i).getBoolean("isSharedPlaylist"));
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

    public void getAllOwnedPlaylists() {
        playlist.clear();
        db.getAllOwnedPlaylists(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray playlistObject = (JSONArray) response.get("playlists");
                    for (int i = 0; i < playlistObject.length(); i++) {
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"), username, false, playlistObject.getJSONObject(i).getBoolean("isSharedPlaylist"));
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
                    JSONArray playlistObject = (JSONArray) response.get("playlists");
                    JSONArray ownerObject = (JSONArray) response.get("listOfPlaylistOwners");
                    ArrayList<String> listOfOwners = new ArrayList<>();

                    if(playlistObject.length() != ownerObject.length()){
                        System.out.println("Error! Playlist length is not equal to owner name list!");
                    }
                    if(playlistObject.length() == 0 || ownerObject.length() == 0){
                        m_playlist.setValue(playlist);
                        return;
                    }
                    for(int i = 0; i < ownerObject.length(); i++){
                        listOfOwners.add(ownerObject.getString(i));
                    }

                    for (int i = 0; i < playlistObject.length(); i++) {
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"), listOfOwners.get(i), false, playlistObject.getJSONObject(i).getBoolean("isSharedPlaylist"));
                        playlist.add(p);
                    }
                    getDefaultPlaylist();
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
                    playlist.add(new Playlist(name, playlist_info.getString("_id"), username, false, false));
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
                    playlist.add(new Playlist(name, playlist_info.getString("_id"), username, false, true));
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
                Toast.makeText(application, application.getApplicationContext().getString(R.string.owner_right_rename), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(application, application.getApplicationContext().getString(R.string.owner_right_delete), Toast.LENGTH_SHORT).show();
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
                        Playlist p = new Playlist(playlistObject.getJSONObject(i).getString("name"), playlistObject.getJSONObject(i).getString("_id"),username, false, playlistObject.getJSONObject(i).getBoolean("isSharedPlaylist"));
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

    public void setAsShare(int position){
        db.setAsShare(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token, playlist.get(position).getPlaylist_id());
    }

}