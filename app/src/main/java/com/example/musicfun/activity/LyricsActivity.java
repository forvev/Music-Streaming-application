package com.example.musicfun.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityLyricsBinding;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.PassDataInterface;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;

import java.util.ArrayList;
import java.util.List;

public class LyricsActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityLyricsBinding binding;
    private MusicbannerService service;
    private boolean isBound;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> artist = new MutableLiveData<>();
    private MutableLiveData<Boolean> session = new MutableLiveData<>();
    private MutableLiveData<String> playlistID = new MutableLiveData<>();

    private ExoPlayer player;
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;
    private List<MediaItem> mediaItems = new ArrayList<>();
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creates a full screen view and hides the default action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sp = getSharedPreferences("login",MODE_PRIVATE);

        binding = ActivityLyricsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.setValue(extras.getString("title"));
            artist.setValue(extras.getString("artist"));
            session.setValue(extras.getBoolean("listenTogether"));
            playlistID.setValue(extras.getString("playlistID"));
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_lyrics);
        navController.navigate(R.id.lyricsFragment);

    }

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            MusicbannerService.ServiceBinder binder = (MusicbannerService.ServiceBinder) iBinder;
            service = binder.getMusicbannerService();
            player = service.player;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    public MutableLiveData<String> getSongTitle(){
        return title;
    }

    public MutableLiveData<String> getPlaylistID(){
        return playlistID;
    }

    public MutableLiveData<String> getSongArtist(){
        return artist;
    }

    public MutableLiveData<Boolean> getSession() {return session;}

    @Override
    public void onStart() {
        // Bind to LocalService
        Intent musicbannerServiceIntent = new Intent(this, MusicbannerService.class);
        bindService(musicbannerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
        super.onStart();
    }


    @Override
    public void onStop() {
        doUnbindService();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    private void doUnbindService(){
        if (isBound){
            unbindService(playerServiceConnection);
            isBound = false;
//            After this activity finished, service will be rebound to other activities.
//            Set startPosition to 0, so that the songs in other activities/fragments can be played from the start.
//            sp.edit().putLong("startPosition", 0).apply();
        }
    }

    private void createMediaItems(List<Songs> playlist) {
        // if the app just started, the songs in the new releases are set as default playlist
        mediaItems.clear();
        for(int i = 0; i < playlist.size(); i++){
            Songs s = playlist.get(i);
            MediaMetadata m = new MediaMetadata.Builder()
                    .setTitle((s.getSongName()))
                    .setArtist(s.getArtist())
                    .setDescription(s.getSongId())
                    .build();
            MediaItem mediaItem = new MediaItem.Builder().setUri("http://10.0.2.2:3000/songs/" + s.getSongId() + "/output.m3u8")
                    .setMediaId(Integer.toString(i))
                    .setMediaMetadata(m)
                    .build();
            mediaItems.add(mediaItem);
        }
        if(isBound){
            service.setPlaylist(mediaItems, startItemIndex, startPosition, startAutoPlay);
        }
    }

    @Override
    public void playSong(List<Songs> playlist, int repeatMode, boolean shuffle) {
        startAutoPlay = true;
        startPosition = 0;
        startItemIndex = 0;
        createMediaItems(playlist);
        player.setRepeatMode(repeatMode);
        player.setShuffleModeEnabled(shuffle);
    }

    @Override
    public void changePlayMode(int repeatMode, boolean shuffle) {
        player.setRepeatMode(repeatMode);
        player.setShuffleModeEnabled(shuffle);
    }

    @Override
    public void seek(List<Songs> playlist, long startPosition, int startItemIndex) {
        this.startAutoPlay = false;
        this.startPosition = startPosition;
        this.startItemIndex = 0;
        createMediaItems(playlist);
    }

    private View.OnClickListener backPress = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackPressed();
        }
    };

    public View.OnClickListener getBackPress (){
        return backPress;
    }

}
