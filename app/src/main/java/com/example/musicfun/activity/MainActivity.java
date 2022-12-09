package com.example.musicfun.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityMainBinding;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.ServerCallBack;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    //playing stuff
    static ExoPlayer player;
    ProgressBar progressBar;

    String url;

    private int timestamp = 0;
    private int progressStatus = 0;
    int currentSongID = 1;

    long realDuration = 100;

    private boolean startingNewSong = false;
    private boolean playing;

    private Handler handler = new Handler();
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creates a full screen view and hides the default action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //finding different UI, R - resources
        BottomNavigationView navView = findViewById(R.id.nav_view);
        progressBar = binding.progressBarSong;
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        sp = getSharedPreferences("login",MODE_PRIVATE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.discovery, R.id.my_music, R.id.friends).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        if(sp.getInt("logged",0) == 0){
            binding.navView.getMenu().removeItem(R.id.friends);
        }
        NavigationUI.setupWithNavController(binding.navView, navController);
        currentSongID = sp.getInt("lastSongID", 1);
        timestamp = sp.getInt("lastSongTimestamp", 0);
        realDuration = sp.getLong("realDuration", 100);
        progressBar.setMax((int) realDuration * 10);
        progressStatus = timestamp / 100;
        url = "http://10.0.2.2:3000/songs/" + currentSongID + "/output.m3u8";
        progressBar.setProgress(progressStatus);
    }


    @Override
    protected void onStop() {
        sp.edit().putInt("lastSongID",currentSongID).apply();
        sp.edit().putInt("lastSongTimestamp",timestamp).apply();
        sp.edit().putLong("realDuration",realDuration).apply();
        super.onStop();
    }


    // TODO: This function should be placed somewhere else instead of MainActivity
    public void playFile(View v){

        //checks if there was a song being played before to stop the thread tracking the last songs progress
        if (startingNewSong) {
            player.pause();
            playing = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //creating desired descenation
        // changing an icon
        ImageView my_icon = findViewById(R.id.play_button);
        my_icon.setImageResource(R.drawable.ic_baseline_pause_24);

        if(playing == false)  {
            startingNewSong = false;
            my_icon.setImageResource(R.drawable.ic_baseline_pause_24);
            playing = true;

            Uri uri = Uri.parse(url);
            player = new ExoPlayer.Builder(this).build();
            MediaItem mediaItem = MediaItem.fromUri(uri);

            // Create a data source factory.
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
            // Create a HLS media source pointing to a playlist uri.
            HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);

            player.setMediaSource(hlsMediaSource);
            player.seekTo(timestamp);
            player.prepare();
            player.play();
            player.addListener(new ExoPlayer.Listener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_READY) {
                        realDuration = player.getDuration()/1000;
                        progressBar.setMax((int) realDuration * 10);
                        // Start long running operation in a background thread
                        new Thread(new Runnable() {
                            public void run() {
                                while (progressStatus < realDuration * 10 && playing) {
                                    progressStatus += 1;
                                    timestamp += 100;
                                    // Update the progress bar
                                    handler.post(new Runnable() {
                                        public void run() {
                                            progressBar.setProgress(progressStatus);
                                        }
                                    });
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }).start();
                    }
                }
            });
            // after playing the song: information are send to the server and the progress bar is reset
            player.addListener(new ExoPlayer.Listener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_ENDED) {
                        playing = false;
                        timestamp = (int) player.getContentDuration();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        timestamp = 0;
                        progressBar.setProgress(0);
                        progressStatus = 0;
                    }
                }
            });
        }
        else {
            my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            playing = false;
            player.pause();
        }
    }

    @Override
    public void sendInput(String data) {
        url = "http://10.0.2.2:3000/songs/" + data + "/output.m3u8";
        currentSongID = Integer.parseInt(data);
        if (playing) {
            startingNewSong = true;
            //TODO: Send last heard song id and timestamp to server (to track what songs the user likes)
            if (timestamp > 10000) {
                String urlListenHistory = "http://10.0.2.2:3000/account/addListenHistory?auth_token=" + sp.getString("token", "");
                JSONObject heardSong = new JSONObject();
                try {
                    heardSong.put("songID", currentSongID);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlListenHistory, heardSong, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //callBack.onSuccess(response);
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //callBack.onError(error);
                    }
                });
                requestQueue.add(request);
            }
        }
        timestamp = 0;
        progressBar.setProgress(0);
        progressStatus = 0;
        playFile(null);
    }

    public boolean isConnectedToServer(String url, int timeout) {
        try{
            URL myUrl = new URL(url);
            URLConnection connection = myUrl.openConnection();
            connection.setConnectTimeout(timeout);
            connection.connect();
            return true;
        } catch (Exception e) {
            // Handle your exceptions
            return false;
        }
    }

    public static void stopMusic() {
        player.pause();
    }
}