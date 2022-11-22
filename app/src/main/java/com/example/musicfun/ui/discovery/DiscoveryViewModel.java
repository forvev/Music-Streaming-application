package com.example.musicfun.ui.discovery;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.Database;
import com.example.musicfun.search.Songs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DiscoveryViewModel extends AndroidViewModel {

    MutableLiveData<ArrayList<Songs>> songNames;
    ArrayList<Songs> songsArrayList;
    String url = "http://10.0.2.2:3000/getallsongs";
    Application application;
    Database db;

    public DiscoveryViewModel(Application application) throws JSONException{
        super(application);
        songNames = new MutableLiveData<>();
        this.application = application;
        db = new Database(application.getApplicationContext());
        init();
    }

    public MutableLiveData<ArrayList<Songs>> getSongNames(){
        return songNames;
    }

    public void init() throws JSONException {
        songsArrayList = db.sendMsg("song_titles", getApplication().getApplicationContext());
    }


}