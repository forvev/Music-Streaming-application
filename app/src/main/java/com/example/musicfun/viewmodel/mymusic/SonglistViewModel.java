package com.example.musicfun.viewmodel.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.R;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.PlaylistRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Set of request to:
 * 1. Get songs from the specific playlist,
 * 2. Delete/add songs from the playlist,
 * 3. Search songs
 *
 */
public class SonglistViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Songs>> m_songlist;
    private MutableLiveData<ArrayList<Songs>> m_searchResult;
    private ArrayList<Songs> songlist;
    private ArrayList<Songs> searchResult;
    private Application application;
    private PlaylistRepository playlistRepository;
    private SharedPreferences sp;
    private String token;

    public SonglistViewModel(Application application) {
        super(application);
        m_songlist = new MutableLiveData<>();
        m_searchResult = new MutableLiveData<>();
        this.application = application;
        playlistRepository = new PlaylistRepository(application.getApplicationContext());
        songlist = new ArrayList<>();
        m_songlist.setValue(songlist);
        searchResult = new ArrayList<>();
        m_searchResult.setValue(searchResult);
        sp = getApplication().getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        token = sp.getString("token", "");
    }

    public MutableLiveData<ArrayList<Songs>> getM_songlist(){
        return m_songlist;
    }
    public MutableLiveData<ArrayList<Songs>> getM_searchResult(){
        return m_searchResult;
    }

    public void getSongsFromPlaylist (String selected_playlist_id) {
        songlist.clear();
        playlistRepository.getSongsFromPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songlistArray = (JSONArray) response.get("songs");
                    for (int j = 0; j < songlistArray.length(); j++){
                        Songs s = new Songs(songlistArray.getJSONObject(j).getString("title"), songlistArray.getJSONObject(j).getString("artist"), songlistArray.getJSONObject(j).getString("_id"));
                        songlist.add(s);
                    }
                    m_songlist.setValue(songlist);
                    m_searchResult.setValue(songlist);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, selected_playlist_id, token);
    }

    public void deleteSongsFromPlaylist (String selected_playlist_id, int position) {
        String song_id = songlist.get(position).getSongId();

        playlistRepository.deleteSongsFromPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                songlist.remove(position);
                m_songlist.setValue(songlist);
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, selected_playlist_id, song_id, token);
    }

    public void addSongToPlaylist(String playlist_position, String song_id){
        playlistRepository.addSongToPlaylist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
            }
            @Override
            public void onError(VolleyError error) {
                if (error.networkResponse.statusCode == 422){
                    Toast.makeText(application, application.getApplicationContext().getString(R.string.already_in_playlist), Toast.LENGTH_SHORT).show();
                }
            }
        }, playlist_position, song_id, token);
    }

    public void searchSongByName(String name, String selected_playlist_id){
        searchResult.clear();
        playlistRepository.searchSongListByName(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray songTitles = (JSONArray) response.get("songs");
                    for (int i = 0; i < songTitles.length(); i++) {
                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getString("_id"));
                        searchResult.add(s);
                    }
                    m_searchResult.setValue(searchResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VolleyError error) {

            }
        }, selected_playlist_id, name, token);
    }

}
