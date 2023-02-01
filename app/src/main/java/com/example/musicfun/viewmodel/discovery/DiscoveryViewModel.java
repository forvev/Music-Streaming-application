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
import com.example.musicfun.R;
import com.example.musicfun.repository.PlaylistRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This class serves as an intermediate station for discovery related database accesses.
 * It is used in MainActivity, LyricsFragment and in every class in fragment -> discovery folder.
 */
public class DiscoveryViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<Songs>> songNames;
    MutableLiveData<ArrayList<Songs>> initList;
    MutableLiveData<Boolean> isInDefault;
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

        isInDefault = new MutableLiveData<>();
        isInDefault.setValue(false);
        SharedPreferences sp = application.getSharedPreferences("login",MODE_PRIVATE);
        token = sp.getString("token", "");
    }

    public MutableLiveData<ArrayList<Songs>> getSongNames(){
        return songNames;
    }

    public MutableLiveData<ArrayList<Songs>> getInitList(){
        return initList;
    }

    public MutableLiveData<Boolean> getIsInDefault() {return isInDefault;}

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
                        boolean alreadyAdded = false;
                        for (int j = 0; j < songsArrayList.size(); j++) {
                            if (songsArrayList.get(j).getSongId().equals(s.getSongId())) {
                                alreadyAdded = true;
                            }
                        }
                        if (!alreadyAdded) {
                            songsArrayList.add(s);
                        }
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

    public void getDefaultPlaylist(String song_id){
        playlistRepository.getDefaultPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String defaultPlaylist = result.getString("defaultPlaylist");
                    addSongToPlaylist(defaultPlaylist, song_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    public void checkSongInDefault(String song_id){
        isInDefault.setValue(false);
        playlistRepository.getDefaultPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String defaultPlaylist = result.getString("defaultPlaylist");
                    checkSongInPlaylist(defaultPlaylist, song_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    public void removeSongFromDefault(String song_id){
        playlistRepository.getDefaultPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String defaultPlaylist = result.getString("defaultPlaylist");
                    removeSongFromPlaylist(defaultPlaylist, song_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    private void checkSongInPlaylist(String selected_playlist_id, String song_id) {
        playlistRepository.getSongsFromPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songlistArray = (JSONArray) response.get("songs");
                    for (int j = 0; j < songlistArray.length(); j++){
                        if(song_id.equals(songlistArray.getJSONObject(j).getString("_id"))){
                            isInDefault.setValue(true);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, selected_playlist_id, token);
    }

    public void addSongToPlaylist(String playlist_id, String song_id){
        playlistRepository.addSongToPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
            }
            @Override
            public void onError(VolleyError error) {
                if (error.networkResponse.statusCode == 422){
                    Toast.makeText(application.getApplicationContext(), application.getApplicationContext().getString(R.string.already_in_playlist), Toast.LENGTH_SHORT).show();
                }
            }
        }, playlist_id, song_id, token);
    }

    public void removeSongFromPlaylist (String selected_playlist_id, String song_id) {
        playlistRepository.deleteSongsFromPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
            }
            @Override
            public void onError(VolleyError error) {
            }
        }, selected_playlist_id, song_id, token);
    }

}