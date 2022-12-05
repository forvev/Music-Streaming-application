package com.example.musicfun.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.android.exoplayer2.ExoPlaybackException;
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
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    //playing stuff
    ExoPlayer player;
    ProgressBar progressBar;

    String url = "http://10.0.2.2:3000/songs/1/output.m3u8";

    private int timeStamp = 0;
    private int progressStatus = 0;
    int currentSongID = 0;

    private boolean startingNewSong = false;
    private boolean playing;

    private Handler handler = new Handler();


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
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.discovery, R.id.my_music, R.id.friends)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //TODO: load last heard song id and timestamp from server
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
            player.seekTo(timeStamp);
            player.prepare();
            player.play();
            player.addListener(new ExoPlayer.Listener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_READY) {
                        long realDuration = player.getDuration()/1000;
                        progressBar.setMax((int) realDuration * 10);
                        // Start long running operation in a background thread
                        new Thread(new Runnable() {
                            public void run() {
                                while (progressStatus < realDuration * 10 && playing) {
                                    progressStatus += 1;
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
                        timeStamp = (int) player.getContentDuration();
                        System.out.println("Content : " + timeStamp);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //TODO: Send last heard song id and timestamp to server (to track what songs the user likes)
                        //if (timeStamp > 10000) -> send id and timestamp to server
                        my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        timeStamp = 0;
                        progressBar.setProgress(0);
                        progressStatus = 0;
                    }
                }
            });
        }
        else {
            my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            playing = false;
            timeStamp = (int) player.getCurrentPosition();
            player.pause();
        }
    }

    @Override
    public void sendInput(String data) {
        url = "http://10.0.2.2:3000/songs/" + data + "/output.m3u8";
        currentSongID = Integer.parseInt(data);
        if (playing) {
            startingNewSong = true;
            timeStamp = (int) player.getCurrentPosition();
            System.out.println(timeStamp);
            //TODO: Send last heard song id and timestamp to server (to track what songs the user likes)
            //if (timeStamp > 10000) -> send id and timestamp to server
        }
        timeStamp = 0;
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
}
