package com.example.musicfun.viewmodel.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.PlaylistRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SonglistViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Songs>> m_songlist;
    private MutableLiveData<ArrayList<Songs>> m_searchResult;
    private ArrayList<Songs> songlist;
    private ArrayList<Songs> searchResult;
    private Application application;
    private PlaylistRepository playlistRepository;
    private SharedPreferences sp;
    private String token;

    public SonglistViewModel(Application application) throws JSONException {
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
        //token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7Il9pZCI6IjYzOTc5NGY1ZGQ2OTAyMGIwMTVlNDBhNSJ9LCJpYXQiOjE2NzA4Nzk4NDh9.0jHIAosQ7HrSA9LZmv1VfD3OWR5gfUfc4Fsa3QzVv3A";

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
                    Toast.makeText(application, "This song was already added to your playlist", Toast.LENGTH_SHORT).show();
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
