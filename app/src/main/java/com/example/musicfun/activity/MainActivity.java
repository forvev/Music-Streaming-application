package com.example.musicfun.activity;

import static com.example.musicfun.activity.MusicbannerService.COPA_RESULT;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityMainBinding;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.viewmodel.MainActivityViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";

    private SharedPreferences sp;

    protected @Nullable ExoPlayer player;
    protected PlayerControlView control;

    private List<MediaItem> mediaItems = new ArrayList<>();
    private List<Songs> songInfo = new ArrayList<>();
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;
    private MainActivityViewModel viewModel;
    private Toolbar toolbar;
    private TextView tv_title;
    private TextView tv_artist;
    private boolean isBound;
    private MusicbannerService service;
    private BroadcastReceiver broadcastReceiver;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creates a full screen view and hides the default action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp = getSharedPreferences("login",MODE_PRIVATE);

//        TODO: move search view and setting to the custom toolbar
        toolbar = binding.toolbar;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.discovery, R.id.my_music, R.id.friends).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // hide fragments if user didn't login
        if(sp.getInt("logged",0) == 0){
            binding.navView.getMenu().removeItem(R.id.friends);
            binding.navView.getMenu().removeItem(R.id.my_music);
        }
        NavigationUI.setupWithNavController(binding.navView, navController);

        // initialize the relative UI components
        tv_title = findViewById(R.id.banner_song_title);
        tv_artist = findViewById(R.id.banner_artist);
        control = findViewById(R.id.controls);
        viewModel = new MainActivityViewModel(getApplication());

        //update UI from based on the information passed from service
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra("title");
                String artist = intent.getStringExtra("artist");
                tv_title.setText(title);
                tv_artist.setText(artist);
            }
        };
        // check whether any songs were saved in the playlist
        String serializedObject = sp.getString("saved_playlist", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Songs>>(){}.getType();
            songInfo = gson.fromJson(serializedObject, type);
            createMediaItems(songInfo);
        }
        else {
            createMediaItems(null);
        }
        startAutoPlay = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_appbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver), new IntentFilter(COPA_RESULT));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
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

    // fetch songs to the playlist
    private List<MediaItem> createMediaItems(List<Songs> playlist) {
        songInfo = playlist;
        // if the app just started, the songs in the new releases are set as default playlist
        if(playlist == null){
            viewModel.init("get/recentlyUploadedSongs");
            viewModel.getSongNames().observe(this, new Observer<ArrayList<Songs>>() {
                @Override
                public void onChanged(@Nullable final ArrayList<Songs> newName) {
                    // binds the Adapter to the ListView
                    songInfo = newName;
                    for(int i = 0; i < newName.size(); i++){
                        Songs s = newName.get(i);
                        MediaMetadata m = new MediaMetadata.Builder()
                                .setTitle((s.getSongName()))
                                .setArtist(s.getArtist())
                                .setDescription(s.getSongId())
                                .build();
                        MediaItem mediaItem = new MediaItem.Builder().setUri("http://10.0.2.2:3000/songs/" + s.getSongId() + "/output.m3u8")
//                                .setMediaId(Integer.toString(i))
                                .setMediaMetadata(m)
                                .build();
                        mediaItems.add(mediaItem);
                    }
                    initializePlayer();
                }
            });
        }
        // otherwise, the parameter indicates the playlist
        else{
            mediaItems.clear();
            for(int i = 0; i < playlist.size(); i++){
                Songs s = playlist.get(i);
                MediaMetadata m = new MediaMetadata.Builder()
                        .setTitle((s.getSongName()))
                        .setArtist(s.getArtist())
                        .setDescription(s.getSongId())
                        .build();
                MediaItem mediaItem = new MediaItem.Builder().setUri("http://10.0.2.2:3000/songs/" + s.getSongId() + "/output.m3u8")
//                        .setMediaId(Integer.toString(i))
                        .setMediaMetadata(m)
                        .build();
                mediaItems.add(mediaItem);
            }
            initializePlayer();
        }
        return mediaItems;
    }

    protected boolean initializePlayer() {

        if(!isBound){
            startItemIndex = sp.getInt("startItemIndex", 0);
            startPosition = sp.getLong("startPosition", 0);
            doBindService();
        }
        else{
            service.setSongInfo(songInfo);
            service.setPlaylist(mediaItems, startItemIndex, startPosition, startAutoPlay);
        }
        return true;
    }

    private void doBindService(){
        Intent MusicbannerServiceIntent = new Intent(this, MusicbannerService.class);
        bindService(MusicbannerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicbannerService.ServiceBinder binder = (MusicbannerService.ServiceBinder) iBinder;
            service = binder.getMusicbannerService();
            player = service.player;
            isBound = true;
            //Get the initialized player from service
            intent = new Intent(getApplicationContext(), MusicbannerService.class);
            startService(intent);
            service.setPlaylist(mediaItems, startItemIndex, startPosition, startAutoPlay);
            service.setSongInfo(songInfo);
            control.setPlayer(player);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("the service was killed! ");
        }
    };

    @Override
    public void playSong(List<Songs> playlist, int repeatMode, boolean shuffle) {
        startAutoPlay = true;
        startPosition = 0;
        startItemIndex = 0;
        createMediaItems(playlist);
        player.setRepeatMode(repeatMode);
        player.setShuffleModeEnabled(shuffle);
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
