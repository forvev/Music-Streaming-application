package com.example.musicfun.activity;

import android.net.Uri;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.R;
import com.example.musicfun.databinding.ActivityMainBinding;
import com.example.musicfun.interfaces.PassDataInterface;
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
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    String url = "http://localhost:3000/testconnection";
    private boolean playing;
//    String url = "http://10.0.2.2:3000/?song_name=o_tannenbaum";
    //playing stuff
    ExoPlayer player;
    int timeStamp = 0;

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

//        replaceFragment(new Discovery_Decision_Fragment());
//        binding.navView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()){
//                case R.id.discovery:
//                    replaceFragment(new Discovery_Decision_Fragment());
//                    break;
//                case R.id.friends:
//                    replaceFragment(new SimpleFriendsFragment());
//                    break;
//                case R.id.my_music:
//                    replaceFragment(new SimpleMyMusicFragment());
//                    break;
//            }
//            return true;
//        });
    }

//    private void replaceFragment(Fragment fragment){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fragmentsframeLayout,fragment);
//        fragmentTransaction.commit();
//    }

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

            //String url = "https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8";
            //String url = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
            //String url = "http://moctobpltc-i.akamaihd.net/hls/live/571329/eight/playlist.m3u8";


            //DefaultRenderersFactory defaultRenderersFactory = new DefaultRenderersFactory(this).forceDisableMediaCodecAsynchronousQueueing();
            String url = "http://10.0.2.2:3000/songs/1/output.m3u8";
            Uri uri = Uri.parse(url);
            player = new ExoPlayer.Builder(this).build();
            MediaItem mediaItem = MediaItem.fromUri(uri);

            // Create a data source factory.
            DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
            // Create a HLS media source pointing to a playlist uri.
            HlsMediaSource hlsMediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);





            //player.setMediaItem(mediaItem);
            player.setMediaSource(hlsMediaSource);
            player.seekTo(timeStamp);
            player.prepare();
            player.play();
        }
        else {
            my_icon.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            playing = false;
            timeStamp = (int) player.getCurrentPosition();
            player.pause();
        }
    }

    // establish connection to server
    public void sendMsg(View v) throws JSONException {
        JSONObject item = new JSONObject();
        item.put("song_name", "o_tannenbaum");
        System.out.println("Im here");
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.GET, url, item, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO: handle response from server
                //JSonObject jsonObject = response.getJSONObject("");

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        ExampleRequestQueue.add(ExampleRequest);
    }

    @Override
    public void sendInput(String data) {
        Log.d(TAG, "sendInput: got the input " + data);
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