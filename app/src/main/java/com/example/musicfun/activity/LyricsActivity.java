package com.example.musicfun.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityLyricsBinding;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.fragment.login.SettingFragment;
import com.example.musicfun.interfaces.PassDataInterface;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LyricsActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityLyricsBinding binding;
    private Toolbar toolbar;
    private MusicbannerService service;
    private boolean isBound;
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> artist = new MutableLiveData<>();

    private ExoPlayer player;
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;
    private List<MediaItem> mediaItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creates a full screen view and hides the default action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityLyricsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        toolbar = binding.toolbar;
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_downward_24));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title.setValue(extras.getString("title"));
            artist.setValue(extras.getString("artist"));
        }
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

    public MutableLiveData<String> getSongArtist(){
        return artist;
    }

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
