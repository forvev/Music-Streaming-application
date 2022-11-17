package com.example.musicfun;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.ui.discovery.DiscoveryFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    String url = "http://10.0.2.2:3000/?song_name=o_tannenbaum";
//    The sendMsg() works with the following string:
//    String url = "https://reqres.in/api/users?page=2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creates a full screen view and hides the default action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new SimpleDiscoveryFragment());

        binding.navView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.discovery:
                    replaceFragment(new Discovery_Decision_Fragment());
                    break;
                case R.id.friends:
                    replaceFragment(new SimpleFriendsFragment());
                    break;
                case R.id.my_music:
                    replaceFragment(new SimpleMyMusicFragment());
                    break;
            }




            return true;
        });
        /*
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.discovery, R.id.my_music, R.id.friends)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        */
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsframeLayout,fragment);
        fragmentTransaction.commit();
    }
    // TODO: This function should be placed somewhere else instead of MainActivity
    // TODO: Stop playing the music by pressing the button again
    public void playFile(View v){
        MediaPlayer mediaPlayer = new MediaPlayer();
        try{
//             Is .mp3 a must?
            mediaPlayer.setDataSource("https://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3");
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync();
        }catch (IOException e){
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
}