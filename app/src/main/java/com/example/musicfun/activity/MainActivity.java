package com.example.musicfun.activity;

import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_AUTO;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_REPEAT;
import static com.google.android.exoplayer2.Player.MEDIA_ITEM_TRANSITION_REASON_SEEK;
import static com.google.android.exoplayer2.Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED;
import static com.google.android.exoplayer2.Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.example.musicfun.viewmodel.MainActivityViewModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.Tracks;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionParameters;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.EventLogger;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PassDataInterface, PlaylistModes {

    private static final String KEY_ITEM_INDEX = "item_index";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";


    private ActivityMainBinding binding;
    public static Activity mainActivity;
    private static final String TAG = "MainActivity";

    //playing stuff
    String url;
    private int timestamp = 0;
    private int progressStatus = 0;
    int currentSongID = 1;

    long realDuration = 100;
    private SharedPreferences sp;

    // new attributes that we need
    protected @Nullable ExoPlayer player;
    protected PlayerControlView control;

    private List<MediaItem> mediaItems = new ArrayList<>();
    private List<Songs> songInfo = new ArrayList<>();
    private TrackSelectionParameters trackSelectionParameters;
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;
    private MainActivityViewModel viewModel;
    private TextView title;
    private TextView artist;

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
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        sp = getSharedPreferences("login",MODE_PRIVATE);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.discovery, R.id.my_music, R.id.friends).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        if(sp.getInt("logged",0) == 0){
            binding.navView.getMenu().removeItem(R.id.friends);
            binding.navView.getMenu().removeItem(R.id.my_music);
        }
        NavigationUI.setupWithNavController(binding.navView, navController);

//        Normally this play button should be clicked only when user what to pause a song or user just start the app
        currentSongID = sp.getInt("lastSongID", 1);
        timestamp = sp.getInt("lastSongTimestamp", 0);
        realDuration = sp.getLong("realDuration", 100);
        url = "http://10.0.2.2:3000/songs/" + currentSongID + "/output.m3u8";

        // initialize the relative UI components
        title = findViewById(R.id.banner_song_title);
        artist = findViewById(R.id.banner_artist);
        control = findViewById(R.id.controls);
        viewModel = new MainActivityViewModel(getApplication());

//       TODO: Close the main activity from outside so that the music is stopped
        mainActivity = this;
//        Intent intent = this.getIntent();
//        if(intent.getExtras() != null && !intent.getExtras().getBoolean("keep")){
//            releasePlayer();
//            clearStartPosition();
//            setIntent(intent);
//            MainActivity.this.finish();
//        }
    }

//    @Override
//    protected void onNewIntent(Intent intent)
//    {
//        super.onNewIntent(intent);
//        System.out.println("HERE IS CALLED FROM SETTING!!!!!!!!");
//        if(!intent.getExtras().getBoolean("keep"))
//        {
//            releasePlayer();
//            clearStartPosition();
//            setIntent(intent);
//            MainActivity.this.finish();
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("the main activity was restarted????");
        if (Build.VERSION.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println("the main activity was resumed????");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT <= 23) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    protected boolean initializePlayer() {
        if (player == null) {
            mediaItems = createMediaItems();
            if (mediaItems.isEmpty()) {
                return false;
            }
            player = new ExoPlayer.Builder(/* context= */ this).build();
            player.addListener(new PlayerEventListener());
            player.addAnalyticsListener(new EventLogger());
            player.setAudioAttributes(AudioAttributes.DEFAULT, /* handleAudioFocus= */ true);
            player.setPlayWhenReady(startAutoPlay);
            control.setPlayer(player);
        }
        boolean haveStartPosition = startItemIndex != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startItemIndex, startPosition);
        }
        player.setMediaItems(mediaItems, /* resetPosition= */ !haveStartPosition);
//        player.setRepeatMode(Player.REPEAT_MODE_ONE);
        player.prepare();
        return true;
    }

    private List<MediaItem> createMediaItems() {
        songInfo = viewModel.getListOfSongs();
        for(int i = 0; i < songInfo.size(); i++){
            Songs s = songInfo.get(i);
            MediaItem mediaItem = new MediaItem.Builder().setUri("http://10.0.2.2:3000/songs/" + Integer.toString(s.getSongId()) + "/output.m3u8").setMediaId(Integer.toString(i)).build();
            mediaItems.add(mediaItem);
        }

        return mediaItems;
    }

    public void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            player.release();
            player = null;
//            playerView.setPlayer(/* player= */ null);
            mediaItems = Collections.emptyList();
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            System.out.println("--------------------What info will be saved when activity finishes");
            startAutoPlay = player.getPlayWhenReady();
            System.out.println("startAutoPlay = " + startAutoPlay);
            startItemIndex = player.getCurrentMediaItemIndex();
            System.out.println("startItemIndex = " + startItemIndex);
            startPosition = Math.max(0, player.getContentPosition());
            System.out.println("startPosition = " + startPosition);
        }
    }

    protected void clearStartPosition() {
        startAutoPlay = true;
        startItemIndex = C.INDEX_UNSET;
        startPosition = C.TIME_UNSET;
    }

    private class PlayerEventListener implements Player.Listener {

        @Override
        public void onPlaybackStateChanged(@Player.State int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
            }
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player.seekToDefaultPosition();
                player.prepare();
            } else {
            }
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, @Player.MediaItemTransitionReason int reason) {
            Player.Listener.super.onMediaItemTransition(mediaItem, reason);
            System.out.println("media Item id = " + mediaItem.mediaId);
            int id = Integer.parseInt(mediaItem.mediaId);
            title.setText(songInfo.get(id).getSongName());
            artist.setText(songInfo.get(id).getArtist());
            // detects whether a song in the playlist is finished
            // reset progress bar (maybe let the player pause for two seconds so that everything is reset properly
            if (reason == MEDIA_ITEM_TRANSITION_REASON_PLAYLIST_CHANGED){
                System.out.println("Reason: playlist changed");
            }
            else if(reason == MEDIA_ITEM_TRANSITION_REASON_SEEK){
                System.out.println("Reason: seek");
            }
            else if(reason == MEDIA_ITEM_TRANSITION_REASON_AUTO){
                System.out.println("Reason: automatically changed to the next media item");
            }
            else if(reason == MEDIA_ITEM_TRANSITION_REASON_REPEAT) {
                System.out.println("Reason: media item has been repeated");
            }
            else{
                System.out.println("there are cases where no reason is given");
            }
        }

    }

    @Override
    public void sendInput(String data) {
//        TODO: pass Songs-datatype instead of a String in order to get the name / artist
        ArrayList <Songs> listOfSongNames = new ArrayList<>();
        listOfSongNames.add(new Songs("default name", "default artist", Integer.valueOf(data)));
//        playSong(listOfSongNames, 2, false);
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

//    public static void stopMusic() {
//        player.pause();
//    }

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

    }


}
