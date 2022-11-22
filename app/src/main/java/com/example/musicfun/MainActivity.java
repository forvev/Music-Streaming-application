package com.example.musicfun;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.databinding.ActivityMainBinding;
import com.example.musicfun.interfaces.PassDataInterface;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
//    String url = "http://10.0.2.2:3000/songs/stream/?id=0";
    String url = "http://10.0.2.2:3000/songs/stream/?id=";
    String data = "";
    private boolean playing;
    ExoPlayer player;

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
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.discovery, R.id.my_music, R.id.friends)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    // TODO: This function should be placed somewhere else instead of MainActivity
    // TODO: Continue playing the song after pressing the stop button
    public void playFile(View v){

        //creating desired descenation
        // changing an icon
        ImageView my_icon = findViewById(R.id.play_button);
        my_icon.setImageResource(R.drawable.ic_baseline_pause_24);

        if(playing == false)  {
            my_icon.setImageResource(R.drawable.ic_baseline_pause_24);
            playing = true;
            player = new ExoPlayer.Builder(this).build();

            MediaItem mediaItem = MediaItem.fromUri(appendURL(url, data));

            player.setMediaItem(mediaItem);
            player.prepare();

            player.play();
        }
        else {
            my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            playing = false;
            player.pause();
        }
    }

    private String appendURL (String url, String id){
        return url + id;
    }

    // TODO: wrong url for playing the song
    @Override
    public void sendInput(String data) {
//        Log.d(TAG, "sendInput: got the input " + data);
        this.data = data;
        Log.d(TAG, "sendInput: got the input " + appendURL(url, data));
    }
}