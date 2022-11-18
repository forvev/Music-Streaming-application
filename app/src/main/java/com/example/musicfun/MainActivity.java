package com.example.musicfun;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.musicfun.databinding.ActivityMainBinding;
import com.example.musicfun.interfaces.PassDataInterface;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private static final String TAG = "MainActivity";
    String url = "http://10.0.2.2:3000/testconnection";
    private boolean playing;
//    String url = "http://10.0.2.2:3000/?song_name=o_tannenbaum";
    //playing stuff
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

            String url = "https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3";
            player = new ExoPlayer.Builder(this).build();

            MediaItem mediaItem = MediaItem.fromUri(url);

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

    // establish connection to server
    public void sendMsg(View v) throws JSONException {
        TextView textView = (TextView) findViewById(R.id.text_home);
        JSONObject item = new JSONObject();
        item.put("song_name", "o_tannenbaum");
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest ExampleRequest = new JsonObjectRequest(Request.Method.GET, url, item, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // TODO: handle response from server
                textView.setText("Response is " + response.toString());
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("Error with sending get request to server " + error.getMessage());
            }
        });
        ExampleRequestQueue.add(ExampleRequest);
    }

    @Override
    public void sendInput(String data) {
        Log.d(TAG, "sendInput: got the input " + data);
    }
}