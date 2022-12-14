package com.example.musicfun.activity;

import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_AUTO;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_SEEK;
import static com.google.android.exoplayer2.Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
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
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.PlaylistModes;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PassDataInterface, PlaylistModes {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    //playing stuff
    static ExoPlayer player;
    ProgressBar progressBar;
    ImageButton play_btn;

    String url;

    private int timestamp = 0;
    private int progressStatus = 0;
    int currentSongID = 1;

    long realDuration = 100;

    private boolean startingNewSong = false;
    private boolean playing;

    private Handler handler = new Handler();
    private SharedPreferences sp;

    // new try
    private long currentPosition = 0;

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
        player = new ExoPlayer.Builder(this).build();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.discovery, R.id.my_music, R.id.friends).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        if(sp.getInt("logged",0) == 0){
            binding.navView.getMenu().removeItem(R.id.friends);
            binding.navView.getMenu().removeItem(R.id.my_music);
        }
        NavigationUI.setupWithNavController(binding.navView, navController);

//        Normally this play button should be clicked only when user what to pause a song or user just start the app
        play_btn = binding.playButton;
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!player.isPlaying() && currentPosition == 0){       // if user just starts the app, which means the player is not playing
                    play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
//                    case 1: user just starts the app
                    ArrayList <Songs> listOfSongNames = new ArrayList<>();
//                TODO: fetch all or n songs from the server from this given currentSongID -> current playlist is all or n songs from the server
                    listOfSongNames.add(new Songs("default name", "default artist", currentSongID));
//                playlist will play in an endless loop and not shuffled
                    playSong(listOfSongNames, 2, false);
                }
                else if(!player.isPlaying() && currentPosition != 0){       // case 2: user has paused the app before, which means this button was clicked once
                    play_btn.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    player.seekTo(currentPosition);
                    player.play(); //do i need this line????????????????
                }
                else{
                    play_btn.setImageResource(R.drawable.ic_baseline_pause_24);
//                if user pause the music
//                TODO: pause the music and the progress bar.
                    player.pause(); // will the player listener react to this pause here?????????
                    System.out.println("the play_btn was clicked!!!!!!!!!!!!!" + currentPosition);
                }


            }
        });
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
//    public void playFile(View v){
//
//        //checks if there was a song being played before to stop the thread tracking the last songs progress
//        if (startingNewSong) {
//            player.pause();
//            playing = false;
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        //creating desired descenation
//        // changing an icon
//        ImageView my_icon = findViewById(R.id.play_button);
//        my_icon.setImageResource(R.drawable.ic_baseline_pause_24);
//
//        if(playing == false)  {
//            startingNewSong = false;
//            my_icon.setImageResource(R.drawable.ic_baseline_pause_24);
//            playing = true;
//
//            Uri uri = Uri.parse(url);
//            MediaItem mediaItem = MediaItem.fromUri(uri);
//
//            // Create a data source factory.
//            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
//            // Create a HLS media source pointing to a playlist uri.
//            HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
//
//            player.setMediaSource(hlsMediaSource);
//            player.seekTo(timestamp);
//            player.prepare();
//            player.play();
//            player.addListener(new ExoPlayer.Listener() {
//                @Override
//                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    if (playbackState == ExoPlayer.STATE_READY) {
//                        realDuration = player.getDuration()/1000;
//                        progressBar.setMax((int) realDuration * 10);
//                        // Start long running operation in a background thread
//                        new Thread(new Runnable() {
//                            public void run() {
//                                while (progressStatus < realDuration * 10 && playing) {
//                                    progressStatus += 1;
//                                    timestamp += 100;
//                                    // Update the progress bar
//                                    handler.post(new Runnable() {
//                                        public void run() {
//                                            progressBar.setProgress(progressStatus);
//                                        }
//                                    });
//                                    try {
//                                        Thread.sleep(100);
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }).start();
//                    }
//                }
//            });
//            // after playing the song: information are send to the server and the progress bar is reset
//            player.addListener(new ExoPlayer.Listener() {
//                @Override
//                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                    if (playbackState == ExoPlayer.STATE_ENDED) {
//                        playing = false;
//                        //timestamp = (int) player.getContentDuration();
//                        try {
//                            Thread.sleep(200);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        addSongToListenHistory();
//                        my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
//                        timestamp = 0;
//                        progressBar.setProgress(0);
//                        progressStatus = 0;
//                    }
//                }
//            });
//        }
//        else {
//            my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
//            playing = false;
//            player.pause();
//        }
//    }

    @Override
    public void sendInput(String data) {
        play_btn.setImageResource(R.drawable.ic_baseline_pause_24);
        if (playing) {
            startingNewSong = true;
            if (timestamp > 10000) {
                addSongToListenHistory();
            }
        }
        url = "http://10.0.2.2:3000/songs/" + data + "/output.m3u8";
        currentSongID = Integer.parseInt(data);
        timestamp = 0;
        progressBar.setProgress(0);
        progressStatus = 0;
//        TODO: pass Songs-datatype instead of a String in order to get the name / artist
        ArrayList <Songs> listOfSongNames = new ArrayList<>();
        listOfSongNames.add(new Songs("default name", "default artist", Integer.valueOf(data)));
        playSong(listOfSongNames, 2, false);
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

    public void addSongToListenHistory() {
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

    @Override
    public void repeatMode(ArrayList<Songs> listOfSongs, int repeatMode, boolean shuffle) {
        playSong(listOfSongs, repeatMode, shuffle);
    }

    public void playSong(ArrayList<Songs> listOfSongs, int repeatMode, boolean shuffle) {
//        click one of the songs on a list, the rest of the list will be added to the playlist automatically
//        clear player-playlist each time a user click on a song on a list -> Extension: show the playlist the player currently have
//        save the time stamp each time when the song is paused / app is destroyed
        player.clearMediaItems();
        for(Songs s : listOfSongs){
            String url = "http://10.0.2.2:3000/songs/" + s.getSongId() + "/output.m3u8";
            Uri uri = Uri.parse(url);
            MediaItem mediaItem = MediaItem.fromUri(uri);
            player.addMediaItem(mediaItem);
        }
        player.seekTo(0,0);

        currentPosition = 0;
        player.setRepeatMode(repeatMode);
        if (shuffle){
            player.setShuffleModeEnabled(true);
        }
        player.prepare();
        player.play();
        player.addListener(new Player.Listener() {
            @Override
            public void onMediaItemTransition(@Nullable MediaItem mediaItem, @Player.MediaItemTransitionReason int reason) {
                Player.Listener.super.onMediaItemTransition(mediaItem, reason);
                // detects whether a song in the playlist is finished
                // reset progress bar (maybe let the player pause for two seconds so that everything is reset properly
                if (reason == MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED){
                    System.out.println("media item Transition called?!");
                }
                if(reason == MEDIA_ITEM_TRANSITION_REASON_SEEK){
                    System.out.println("MEDIA_ITEM_TRANSITION_REASON_SEEK");
                }
                if(reason == MEDIA_ITEM_TRANSITION_REASON_AUTO){
                    System.out.println("MEDIA_ITEM_TRANSITION_REASON_AUTO");
                }
                else {
                    System.out.println("MEDIA_ITEM_TRANSITION_REASON_REPEAT");
                }
            }

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Player.Listener.super.onIsPlayingChanged(isPlaying);
                // Not playing because playback is paused, ended, suppressed, or the player is buffering, stopped or failed.
                // Check player.getPlayWhenReady, player.getPlaybackState, player.getPlaybackSuppressionReason and player.getPlaybackError for details.
                if(isPlaying){
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
                else{
                    currentPosition = player.getCurrentPosition();
                }

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Player.Listener.super.onRepeatModeChanged(repeatMode);
            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
                Player.Listener.super.onShuffleModeEnabledChanged(shuffleModeEnabled);
            }

            @Override
            public void onPlayerError(PlaybackException error) {
                Player.Listener.super.onPlayerError(error);
            }

            @Override
            public void onPlayerErrorChanged(@Nullable PlaybackException error) {
                Player.Listener.super.onPlayerErrorChanged(error);
            }

           @Override
           public void onTimelineChanged(Timeline timeline, @Player.TimelineChangeReason int reason) {
               if (reason == TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED) {
                   // Update the UI according to the modified playlist (add, move or remove).
//                   updateUiForPlaylist(timeline);
                   System.out.println("Playlist item changed!!!");
               }
           }
        }

        );
    }

}
