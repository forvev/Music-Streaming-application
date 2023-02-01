package com.example.musicfun.activity;

import static com.example.musicfun.activity.MusicbannerService.COPA_RESULT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.R;
import com.example.musicfun.adapter.search.SearchPlaylistAdapter;
import com.example.musicfun.adapter.search.SearchResultAdapter;
import com.example.musicfun.adapter.search.SearchSonglistAdapter;
import com.example.musicfun.adapter.search.SearchUserResultAdapter;
import com.example.musicfun.databinding.ActivityMainBinding;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.datatype.User;
import com.example.musicfun.fragment.mymusic.MyMusicFragmentDirections;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.CloseSearchViewInterface;
import com.example.musicfun.viewmodel.FriendsViewModel;
import com.example.musicfun.viewmodel.discovery.DiscoveryViewModel;
import com.example.musicfun.viewmodel.mymusic.PlaylistViewModel;
import com.example.musicfun.viewmodel.mymusic.SonglistViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MainActivity is responsible for all search functions in different fragments (search songs, playlists, songs in a playlist and friends),
 *      and does preparation for playlist contents,
 *      and update music banner UI components, when service sends out a broadcast.
 */
public class MainActivity extends BaseActivity implements PassDataInterface {

    private ActivityMainBinding binding;
    private SharedPreferences sp;

    protected @Nullable ExoPlayer player;
    protected PlayerControlView control;

//    service related attributes
    private List<MediaItem> mediaItems = new ArrayList<>();
    private List<Songs> songInfo = new ArrayList<>();
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;
    private DiscoveryViewModel discoveryViewModel;
    private TextView tv_title;
    private TextView tv_artist;
    private boolean isBound;
    private boolean initBound = true;
    private MusicbannerService service;
    private BroadcastReceiver broadcastReceiver;
    private Intent intent;

//    search relevant attributes
    private Toolbar toolbar;
    private SearchView searchView;
    private ListView searchResult;
    private ImageView setting;
    private TextView cancel;
    public NavController navController;
    private String playlistId;

//    For searching friends. The changes of MutableLiveData will be sent back to MainActivity.
//    Friends_friend_Fragment should also listen to changes here in MainActivity.
//    Otherwise the view will not be updated
    public MutableLiveData<ArrayList<User>> reply = new MutableLiveData<>();
    public MutableLiveData<ArrayList<User>> getReply() {return reply;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //creates a full screen view and hides the default action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sp = getSharedPreferences("login",MODE_PRIVATE);

//       initialize toolbar
        toolbar = binding.toolbar;
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//       Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.discovery, R.id.my_music, R.id.friends).build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
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
        control = binding.controls;
        discoveryViewModel = new DiscoveryViewModel(getApplication());

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

//        handel toolbar functions
        searchView = binding.actionSearch;
        initSearchHint(getString(R.string.search_hint_songs));
        searchResult = binding.searchList;
        setting = binding.setting;
        cancel = binding.cancel;

        listenNavController();
        menuSelectedCheck();
        searchViewFocused(false);

//       setting
        setting.setOnClickListener(goToSetting);
        binding.controls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LyricsActivity.class);
                i.putExtra("title",tv_title.getText());
                i.putExtra("artist",tv_artist.getText());
                i.putExtra("listenTogether", false);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed()
    {

    }


    public void listenNavController(){
//        special care for the MyPlaylistFragment and ChooseOnePlaylistFragment
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.myPlaylistFragment){
                    searchView.setQueryHint(getString(R.string.search_hint_song_in_playlist));
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(closeFragment);
                }
//                in ChooseOnePlaylistFragment, the search function searches all songs in the databank
                else if (navDestination.getId() == R.id.choose_one_playlist){
                    searchView.setQueryHint(getString(R.string.search_hint_songs));
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
                    toolbar.setNavigationOnClickListener(closeFragment);
                }
            }
        });
    }

    public void menuSelectedCheck(){
//        ensure the bottom navView always shows the correct color
        binding.navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.discovery) {
                    searchView.setQueryHint(getString(R.string.search_hint_songs));
                }
                else if(item.getItemId() == R.id.my_music) {
                    searchView.setQueryHint(getString(R.string.search_hint_playlist));
                }
                else if (item.getItemId() == R.id.friends) {
                    searchView.setQueryHint(getString(R.string.search_hint_friend));
                }
                else{
                    searchView.setQueryHint(getString(R.string.search_hint_songs));
                }
                NavigationUI.onNavDestinationSelected(item, navController);
                return true;
            }
        });
    }

    public void initSearchHint(String text){
        searchView.setQueryHint(text);
    }

//  newController = false indicates the controller is in MainActivity
//  newController = true indicates the controller is from DiscoveryFragment
    public void searchViewFocused(boolean newController){
//        search function
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    toolbar.setBackgroundColor(getResources().getColor(R.color.purple_50));
                    setting.setVisibility(View.GONE);
                    cancel.setVisibility(View.VISIBLE);
                    cancel.setOnClickListener(cancelSearch);
                    binding.flSearchResult.setVisibility(View.VISIBLE);
                    locateFragment();
                }
            }
        });
    }

//    pop back to the previous fragment. Only used in MyPlaylistFragment & ChooseOnePlaylistFragment
    private View.OnClickListener closeFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            navController.popBackStack();
        }
    };

    private View.OnClickListener cancelSearch = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            closeSearchView(view);
        }
    };

    private View.OnClickListener goToSetting = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(sp.getInt("logged", 999) == 1){
                Intent gotoSetting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(gotoSetting);
            }
            else{
                Intent gotoLogin = new Intent(MainActivity.this, RegisterActivity.class);
                sp.edit().putInt("logged", -1).apply();
                if(player.isPlaying()){
                    player.stop();
                }
                service.notificationManager.setPlayer(null);
                Toast.makeText(getApplicationContext(), getString(R.string.login_required), Toast.LENGTH_SHORT).show();
                startActivity(gotoLogin);
            }
        }
    };

//    find the current visible fragment
    private void locateFragment(){
        int currentFragmentId = navController.getCurrentDestination().getId();
        if(currentFragmentId == R.id.discovery){
            searchSongs();
        }
        else if (currentFragmentId == R.id.choose_one_playlist){
            toolbar.setNavigationOnClickListener(cancelSearch);
            searchSongs();
        }
        else if(currentFragmentId == R.id.my_music){
            searchPlaylist();
        }
        else if(currentFragmentId == R.id.myPlaylistFragment){
            toolbar.setNavigationOnClickListener(cancelSearch);
            searchSongsInPlaylist();
        }
        else if(currentFragmentId == R.id.friends){
            searchFriend();
        }
        else{
            System.out.println("No Fragment is visible in Main activity!");
        }
    }

    /*
    Search a song from all songs in database.
    Possible extension: Instead of fetch all songs from db, fetch the 10 songs
     */
    @SuppressLint("ClickableViewAccessibility")
    private void searchSongs(){
        discoveryViewModel.init("get/allSongs");
        discoveryViewModel.getSongNames().observe(MainActivity.this, new Observer<ArrayList<Songs>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Songs> newName) {
                SearchResultAdapter adapter = new SearchResultAdapter(MainActivity.this, newName);
                searchResult.setAdapter(adapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        discoveryViewModel.filter(newText);
                        return true;
                    }
                });
                songInfo = newName;
            }
        });
        searchResult.setOnTouchListener(touchListener);
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playSong(songInfo.subList(i, songInfo.size()), Player.REPEAT_MODE_ALL);
                closeSearchView(view);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void searchPlaylist(){
        PlaylistViewModel playlistViewModel = new PlaylistViewModel(getApplication());
        playlistViewModel.getAllPlaylists();
        playlistViewModel.getM_searchResult().observe(MainActivity.this, new Observer<ArrayList<Playlist>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Playlist> newName) {
                SearchPlaylistAdapter adapter = new SearchPlaylistAdapter(getApplicationContext(), newName);
                searchResult.setAdapter(adapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        playlistViewModel.searchPlaylistByName(newText);
                        return true;
                    }
                });
            }
        });
        searchResult.setOnTouchListener(touchListener);
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Playlist p = (Playlist) searchResult.getItemAtPosition(i);
                playlistId = p.getPlaylist_id();
                closeSearchView(view);
                NavDirections action = MyMusicFragmentDirections.actionMyMusicToMyPlaylistFragment(playlistId);
                navController.navigate(action);
            }
        });
    }

    public void setPlaylistId(String playlistId){
        this.playlistId = playlistId;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void searchSongsInPlaylist(){
        SonglistViewModel songlistViewModel = new SonglistViewModel(getApplication());
        songlistViewModel.getSongsFromPlaylist(playlistId);
        songlistViewModel.getM_searchResult().observe(MainActivity.this, new Observer<ArrayList<Songs>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Songs> newName) {
                SearchSonglistAdapter searchResultAdapter = new SearchSonglistAdapter(getApplicationContext(), newName);
                // binds the Adapter to the ListView
                searchResult.setAdapter(searchResultAdapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        String text = newText;
                        songlistViewModel.searchSongByName(text, playlistId);
                        return true;
                    }
                });
                songInfo = newName;
            }
        });
        searchResult.setOnTouchListener(touchListener);
        searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                playSong(songInfo.subList(i, songInfo.size()), Player.REPEAT_MODE_ALL);
                closeSearchView(view);
            }
        });
    }

    private void searchFriend(){
        FriendsViewModel friendsViewModel = new FriendsViewModel(getApplication());
        friendsViewModel.initSearch();
        friendsViewModel.getM_searchUserResult().observe(MainActivity.this, new Observer<ArrayList<User>>() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onChanged(@Nullable final ArrayList<User> users) {
                CloseSearchViewInterface closeSearchViewInterface = new CloseSearchViewInterface() {
                    @Override
                    public void updateView(View view) {
                        closeSearchView(view);
                    }
                };
                SearchUserResultAdapter adapter = new SearchUserResultAdapter(getApplicationContext(), users, friendsViewModel, closeSearchViewInterface);
                // binds the Adapter to the ListView
                searchResult.setAdapter(adapter);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String newTest) {
                        String text = newTest;
                        friendsViewModel.filter(text, sp.getString("token", ""));
                        return true;
                    }
                });
            }
        });
        searchResult.setOnTouchListener(touchListener);
        friendsViewModel.getUserNames().observe(MainActivity.this, new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                if(users != null && users.size() != 0){
                    reply.setValue(users);
                }
            }
        });
    }

    public void closeSearchView(View view){
        closeKeyboard(view);
        if (navController.getCurrentDestination().getId() == R.id.myPlaylistFragment || navController.getCurrentDestination().getId() == R.id.choose_one_playlist){
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(closeFragment);
        }
        searchView.setQuery("", false);
        searchView.clearFocus();
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        binding.flSearchResult.setVisibility(View.GONE);
        setting.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
    }


    @SuppressLint("ClickableViewAccessibility")
    private final View.OnTouchListener touchListener = (view, motionEvent) -> {
        closeKeyboard(view);
        return false;
    };

//    set up broadcast to get information from service
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
    private void createMediaItems(List<Songs> playlist) {
        songInfo = playlist;
        // if the app just started, the songs in the new releases are set as default playlist
        if(playlist == null || playlist.isEmpty()){
            discoveryViewModel.initInMain("get/recentlyUploadedSongs");
            discoveryViewModel.getInitList().observe(this, new Observer<ArrayList<Songs>>() {
                @Override
                public void onChanged(@Nullable final ArrayList<Songs> newName) {
                    // binds the Adapter to the ListView
                    if(!newName.isEmpty()){
                        songInfo = newName;
                        for(int i = 0; i < newName.size(); i++){
                            Songs s = newName.get(i);
                            MediaMetadata m = new MediaMetadata.Builder()
                                    .setTitle((s.getSongName()))
                                    .setArtist(s.getArtist())
                                    .setDescription(s.getSongId())
                                    .build();
                            //MediaItem mediaItem = new MediaItem.Builder().setUri("https://10.0.2.2:3000/songs/" + s.getSongId() + "/output.m3u8")
                            MediaItem mediaItem = new MediaItem.Builder().setUri("https://100.110.104.112:3000/songs/" + s.getSongId() + "/output.m3u8")
                                    .setMediaMetadata(m)
                                    .build();
                            mediaItems.add(mediaItem);
                        }
                        initializePlayer();
                    }
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
                //MediaItem mediaItem = new MediaItem.Builder().setUri("https://10.0.2.2:3000/songs/" + s.getSongId() + "/output.m3u8")
                MediaItem mediaItem = new MediaItem.Builder().setUri("https://100.110.104.112:3000/songs/" + s.getSongId() + "/output.m3u8")
//                        .setMediaId(Integer.toString(i))
                        .setMediaMetadata(m)
                        .build();
                mediaItems.add(mediaItem);
            }
            initializePlayer();
        }
    }

//    initialize the player. If it is the initial bind, then fetch data from SharedPreferences.
    protected boolean initializePlayer() {
        System.out.println("isBound? " + (isBound));
        if(!isBound){
            if(initBound){
                startItemIndex = sp.getInt("startItemIndex", 0);
                startPosition = sp.getLong("startPosition", 0);
                initBound = false;
            }
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

    public MusicbannerService getService (){
        return service;
    }

    @Override
    public void playSong(List<Songs> playlist, int repeatMode) {
        startAutoPlay = true;
        startPosition = 0;
        startItemIndex = 0;
        createMediaItems(playlist);
        player.setRepeatMode(repeatMode);
    }

    @Override
    public void changePlayMode(int repeatMode, boolean shuffle) {
        player.setRepeatMode(repeatMode);
        player.setShuffleModeEnabled(shuffle);
    }

    @Override
    public void seek(List<Songs> playlist, long startPosition, int startItemIndex) {
        startAutoPlay = false;
        this.startPosition = startPosition;
        this.startItemIndex = startItemIndex;
        createMediaItems(playlist);
    }

    public void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
