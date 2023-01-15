package com.example.musicfun.fragment.banner;

import static android.content.Context.MODE_PRIVATE;
import static com.example.musicfun.activity.MusicbannerService.COPA_RESULT;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.LyricsActivity;
import com.example.musicfun.activity.MusicbannerService;
import com.example.musicfun.databinding.FragmentLyricsBinding;
import com.example.musicfun.datatype.Lyrics;
import com.example.musicfun.datatype.RelativeSizeColorSpan;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.viewmodel.MainActivityViewModel;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.example.musicfun.datatype.SocketIOClient;
import io.socket.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class LyricsFragment extends Fragment {

    private FragmentLyricsBinding binding;
    private MusicbannerService service;
    protected @Nullable ExoPlayer player;
    private BroadcastReceiver broadcastReceiver;
    private MainActivityViewModel viewModel;

    private StyledPlayerControlView controlView;
    private TextView tv_title;
    private String title = "";
    private TextView tv_artist;
    private String artist = "";
    private ImageView coverView;
    private ImageView btn_currentPlaylist;
    private ImageView btn_active_guests;

    //Socket IO
    SocketIOClient socketIOClient = new SocketIOClient();
    private String room = "test";

    // views and variables for the lyrics
    private List<Lyrics> lyricsList;
    private int currentLine = -1;   // current singing row , should be highlighted.
    private TextView tv_lyrics;
    private boolean isVisible;
    private boolean lyricsExist;
    private final int POLL_INTERVAL_MS_PLAYING = 1000;
    private final int POLL_INTERVAL_MS_PAUSED = 3000;
    int spanColorHighlight = Color.parseColor("#311B92");
    private RelativeSizeColorSpan highlight = new RelativeSizeColorSpan (1.3f, spanColorHighlight);
    private Spannable spannableText;
    private ScrollingMovementMethod scrolltext = new ScrollingMovementMethod();

    //list of connected usernames in synchronized playback
    private ArrayList<String> usernames = new ArrayList<>();
    MutableLiveData<ArrayList<String>> usernamesLive = new MutableLiveData<ArrayList<String>>();

//    Toolbar and buttons
    private Toolbar toolbar;
    private boolean isBound;
    private boolean isSession;

    private SharedPreferences sp;

    //syncrhonized playback
    private boolean playerseek = false;
    private boolean playerpause = false;
    String songID;
    String timestamp;
    String lastTimestamp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLyricsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isVisible = true;
        controlView = binding.playerView;
        controlView.setShowTimeoutMs(0);

        toolbar = binding.toolbarLyrics;
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_downward_24));
        ((LyricsActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((LyricsActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(closeFragment);

//        Initialize the views
//        update title and artist
        tv_title = getView().findViewById(R.id.styled_player_song_name);
        tv_artist = getView().findViewById(R.id.styled_player_artist);
        coverView = getView().findViewById(R.id.imageView2);
        if(!Objects.equals(title, "")){
            tv_title.setText(title);
            tv_artist.setText(artist);
        }

//        lyrics relevant
        tv_lyrics = binding.lyrics;
        tv_lyrics.setMovementMethod(scrolltext);

        btn_currentPlaylist = getView().findViewById(R.id.current_playlist);
        btn_currentPlaylist.setOnClickListener(showCurrentPlaylist);
        btn_active_guests = binding.activeListeners;
        if (isSession) {
            btn_active_guests.setVisibility(View.VISIBLE);
            btn_active_guests.setOnClickListener(showActiveGuests);
        }
        else {
            btn_active_guests.setVisibility(View.GONE);
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                title = intent.getStringExtra("title");
                artist = intent.getStringExtra("artist");
                tv_title.setText(title);
                tv_artist.setText(artist);
                String coverUrl = intent.getStringExtra("coverUrl");
                changeCover(coverUrl);
                updateLyricsFile();
            }
        };

    }

//    Bind service from fragment to make sure the service is bound on time
    private ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            MusicbannerService.ServiceBinder binder = (MusicbannerService.ServiceBinder) iBinder;
            service = binder.getMusicbannerService();

            player = service.player;
            player.addListener(new Player.Listener() {
                @Override
                public void onIsPlayingChanged(boolean isPlaying) {
                    if (!playerseek) {
                        Log.d("test", "onIsPlayingChanged");
                        if (isPlaying) {
                            sendPlayerstate("play", "");
                        } else {
                            playerpause = true;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    Log.d("test", !isPlaying + "");
                                    Log.d("test", playerpause + "");
                                    if (!player.isPlaying() && playerpause) {
                                        sendPlayerstate("pause", "");
                                    }
                                    playerpause = false;
                                }
                            }, 500);
                        }
                    }
                    playerseek = false;
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {
                    sendPlayerstate("repeat", Integer.toString(repeatMode));
                }

                @Override
                public void onPositionDiscontinuity(Player.PositionInfo oldPosition, Player.PositionInfo newPosition, int reason) {
                    if (reason == Player.DISCONTINUITY_REASON_SEEK) {
                        if (playerseek == true) {
                            playerseek = false;
                        }
                        else {
                            Log.d("test", "onPositionDiscontinuity");
                            if (oldPosition.mediaItemIndex == newPosition.mediaItemIndex) {
                                sendPlayerstate("syncTime", Long.toString(newPosition.positionMs));
                            }
                            else {
                                sendPlayerstate("syncSong", Integer.toString(newPosition.mediaItemIndex));
                            }
                        }
                    }
                }
            });
            controlView.setPlayer(player);

            if(player != null){
                String id = Objects.requireNonNull(player.getCurrentMediaItem()).mediaMetadata.description.toString();
                String coverUrl = "http://10.0.2.2:3000/images/" + id + ".jpg";
                Picasso.get().load(coverUrl).into(coverView);
                if (title.equals("") && artist.equals("")){
                    ((LyricsActivity)getActivity()).getSongTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (!s.isEmpty()){
                                tv_title.setText(s);
                            }
                        }
                    });
                    ((LyricsActivity)getActivity()).getSongArtist().observe(getViewLifecycleOwner(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            if (!s.isEmpty()){
                                tv_artist.setText(s);
                            }
                        }
                    });
                    ((LyricsActivity)getActivity()).getSession().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                        @Override
                        public void onChanged(Boolean b) {
                            isSession = b;
                            if (isSession) {
                                ((LyricsActivity)getActivity()).getPlaylistID().observe(getViewLifecycleOwner(), new Observer<String>() {
                                    @Override
                                    public void onChanged(String s) {
                                        if (!s.isEmpty()){
                                            room = s;
                                            controlView.setShowShuffleButton(false);
                                            connectToSocketIO();
                                            JSONObject mess = new JSONObject();
                                            try{
                                                mess.put("msg", "send usernames");
                                                mess.put("username", room);
                                            }catch(JSONException e){
                                                e.printStackTrace();
                                            }
                                            socketIOClient.mSocket.emit("activeUsers",  mess);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                public void run() {
                                                    if (usernames.size() > 1) {
                                                        String person2;
                                                        if (usernames.get(0).equals(sp.getString("name", ""))) {
                                                            person2 = usernames.get(1);
                                                        }
                                                        else {
                                                            person2 = usernames.get(0);
                                                        }
                                                        JSONObject mess2 = new JSONObject();
                                                        try{
                                                            mess2.put("msg", "syncJoin");
                                                            mess2.put("song", "");
                                                            mess2.put("time", "");
                                                            mess2.put("person", person2);
                                                            mess2.put("person2", sp.getString("name", ""));
                                                            mess2.put("username", room);
                                                            mess2.put("playPlaying", false);
                                                        }catch(JSONException e){
                                                            e.printStackTrace();
                                                        }
                                                        socketIOClient.mSocket.emit("syncOnJoin",  mess2);
                                                    }
                                                }
                                            }, 1000);
                                        }
                                    }
                                });
                            }
                            if(btn_active_guests != null && isSession){
                                btn_active_guests.setVisibility(View.VISIBLE);
                                btn_active_guests.setOnClickListener(showActiveGuests);
                            }
                            else if (btn_active_guests != null){
                                btn_active_guests.setVisibility(View.GONE);
                            }
                        }
                    });
                }
                updateLyricsFile();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private View.OnClickListener showCurrentPlaylist = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int startItemIndex = player.getCurrentMediaItemIndex();
            List<Songs> songInfo = service.getSongInfo();
            List<Songs> restOfPlaylist = songInfo.subList(startItemIndex, songInfo.size());
            Gson gson = new Gson();
            String json = gson.toJson(restOfPlaylist);

            NavDirections action = LyricsFragmentDirections.actionLyricsFragmentToCurrentPlaylistFragment(json);
            Navigation.findNavController(getView()).navigate(action);
            isVisible = false;
        }
    };


    private View.OnClickListener showActiveGuests = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Toast.makeText(getContext(), "active listeners clicked!", Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String json = gson.toJson(usernames);
            NavDirections action = LyricsFragmentDirections.actionLyricsFragmentToActiveListenerFragment(json);
            Navigation.findNavController(getView()).navigate(action);
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent musicbannerServiceIntent = new Intent(getActivity(), MusicbannerService.class);
        getActivity().bindService(musicbannerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((broadcastReceiver), new IntentFilter(COPA_RESULT));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        doUnbindService();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        JSONObject mess = new JSONObject();
        try{
            mess.put("msg", "send usernames");
            mess.put("username", room);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socketIOClient.mSocket.emit("activeUsers",  mess);
        socketIOClient.mSocket.emit("end");
        socketIOClient.mSocket.disconnect();
        doUnbindService();
        super.onDestroy();
    }

    private void doUnbindService(){
        if (isBound){
            getActivity().unbindService(playerServiceConnection);
            isBound = false;
        }
    }

    private void updateLyricsFile(){
        lyricsList = new ArrayList<>();
        viewModel.fetchLyrics(player.getCurrentMediaItem().mediaMetadata.description.toString());
        viewModel.getM_songLrc().observe(getViewLifecycleOwner(), new Observer<ArrayList<Lyrics>>() {
            @Override
            public void onChanged(ArrayList<Lyrics> lyrics) {
                if(lyrics.size() != 0){
                    tv_lyrics.setGravity(Gravity.CENTER_HORIZONTAL);
                    lyricsExist = true;
                    lyricsList = lyrics;
                    String allText = "";
                    for (Lyrics l : lyrics){
                        allText = allText + l.getLyrics() + "\n";
                    }
                    tv_lyrics.setText(allText, TextView.BufferType.SPANNABLE);
                    spannableText = (Spannable) tv_lyrics.getText();
                    getCurrentPlayerPosition();
                }
                else{
                    tv_lyrics.scrollTo(0, 0);
                    lyricsExist = false;
                    tv_lyrics.setGravity(Gravity.CENTER);
                    tv_lyrics.setText("No Lyrics");
                }
            }
        });
    }

    // checks the current player position every 500ms
    private void getCurrentPlayerPosition() {
        if(isVisible && lyricsExist){
            long time = player.getCurrentPosition();
            int newLine = findLine(time);
            showCurrentLines(newLine);
            int[] startEnd = currentStartPoint(newLine);
            spannableText.setSpan(highlight, startEnd[0], startEnd[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            currentLine = newLine;

            if (player.isPlaying()) {
                time = player.getCurrentPosition();
                newLine = findLine(time);
                // update UI if line index has been changed
                if (newLine != currentLine){
                    // remove all existing span (old span)
                    spannableText.removeSpan(highlight);
                    // set span for the new line
                    startEnd = currentStartPoint(newLine);
                    spannableText.setSpan(highlight, startEnd[0], startEnd[1], Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    showCurrentLines(newLine);
                    currentLine = newLine;
                }
            }
            controlView.postDelayed(this::getCurrentPlayerPosition, POLL_INTERVAL_MS_PLAYING);
        }
        else if (lyricsExist){
            controlView.postDelayed(this::getCurrentPlayerPosition, POLL_INTERVAL_MS_PAUSED);
        }
    }

    // find the start and end point of the current line. This helps to highlight the lyrics.
    private int[] currentStartPoint(int line){
        int[] result = new int[2];
//        if(lyricsExist){
//            int start = 0;
//            if (line == 0){
//                result[0] = 0;
//                result[1] = lyricsList.get(0).getLength() + 1;
//                return result;
//            }
//            for (int i = 0; i < line; i++){
//                start = start + lyricsList.get(i).getLength() + 1;
//            }
//            result[0] = start;
//            result[1] = start + lyricsList.get(line).getLength();
//        }
        int start = 0;
        if (line == 0){
            result[0] = 0;
            result[1] = lyricsList.get(0).getLength() + 1;
            return result;
        }
        for (int i = 0; i < line; i++){
            start = start + lyricsList.get(i).getLength() + 1;
        }
        result[0] = start;
        result[1] = start + lyricsList.get(line).getLength();
        return result;
    }

    // Move the textview (including the hidden lines) so that the current line is always in the middle of the screen
    private void showCurrentLines(int i){
        int middleOfTextviewHeight = tv_lyrics.getHeight() / 2;
        tv_lyrics.scrollTo(0, -middleOfTextviewHeight + tv_lyrics.getLayout().getLineTop(i));
    }

    // returns the index of the current line of lyrics
    private int findLine(long currentTime){
        int i = 0;
        while(i < lyricsList.size() - 1){
            Lyrics first = lyricsList.get(i);
            Lyrics second = lyricsList.get(i + 1);
            if (currentTime >= first.getStartTime() && currentTime < second.getStartTime()){
                break;
            }
            i++;
        }
        return i;
    }

    public void changeCover(String url){
        Picasso.get().load(url).into(coverView);
    }

    private View.OnClickListener closeFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(isSession){
                AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
                adb.setMessage("Are you sure to leave the room?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                adb.show();
            }
            else{
                getActivity().finish();
            }
        }
    };

    private void sendPlayerstate(String message, String info) {
        JSONObject mess = new JSONObject();
        try{
            mess.put("msg", message);
            mess.put("info", info);
            mess.put("username", room);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socketIOClient.mSocket.emit("sendPlayerstate",  mess);
    }

    private Emitter.Listener onNewPlayerstate = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject jsonmessage = (JSONObject) args[0];
            String message = "";
            String info = "";
            try {
                message = jsonmessage.getString("message");
                info = jsonmessage.getString("info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (message.equals("play")) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        player.play();
                    }
                });
            }
            if (message.equals("pause")) {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        player.pause();
                    }
                });
            }
            if (message.equals("syncSong")) {
                int finalInfo = Integer.parseInt(info);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        playerseek = true;
                        player.seekTo(finalInfo, 0);
                    }
                });
            }
            if (message.equals("syncTime") && !info.equals(lastTimestamp)) {
                lastTimestamp = info;
                long finalInfo = Long.parseLong(info);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        playerseek = true;
                        player.seekTo(finalInfo);
                    }
                });
            }
            if (message.equals("repeat")) {
                Integer finalInfo = Integer.parseInt(info);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        player.setRepeatMode(finalInfo);
                    }
                });
            }
        }
    };

    private Emitter.Listener onNewActiveUsers = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String username = sp.getString("name", "");
            JSONObject jsonmessage = (JSONObject) args[0];
            String message = "";
            try {
                message = jsonmessage.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
           if (message.equals("send usernames")) {
               usernames.clear();
               JSONObject mess = new JSONObject();
               try{
                   mess.put("msg", username);
                   mess.put("username", room);
               }catch(JSONException e){
                   e.printStackTrace();
               }
               socketIOClient.mSocket.emit("activeUsers",  mess);
           }
           else {
               usernames.add(message);
               usernames = filterNames(usernames);

               //usernamesLive.postValue(usernames);
           }
        }
    };

    private Emitter.Listener onNewSyncOnJoin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            String username = sp.getString("name", "");
            JSONObject jsonmessage = (JSONObject) args[0];
            String message = "";
            String song = "";
            String time = "";
            String person = "";
            String person2 = "";
            Boolean playerPlaying  = false;
            try {
                message = jsonmessage.getString("message");
                song = jsonmessage.getString("song");
                time = jsonmessage.getString("time");
                person = jsonmessage.getString("person");
                person2 = jsonmessage.getString("person2");
                playerPlaying = jsonmessage.getBoolean("player");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (message.equals("syncJoin") && username.equals(person)) {
                String finalPerson = person2;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        songID = Integer.toString(player.getCurrentMediaItemIndex());
                        Log.d("test", "songID " + songID);
                        timestamp = Long.toString(player.getCurrentPosition());
                        JSONObject mess = new JSONObject();
                        try{
                            mess.put("msg", "syncJoinBack");
                            mess.put("song", songID);
                            mess.put("time", timestamp);
                            mess.put("person", "");
                            mess.put("player", player.isPlaying());
                            mess.put("person2", finalPerson);
                            mess.put("username", room);
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                        socketIOClient.mSocket.emit("syncOnJoin",  mess);
                    }
                });
            }
            if (message.equals("syncJoinBack") && person2.equals(username)) {
                Integer finalSong = Integer.parseInt(song);
                Long finalTime = Long.parseLong(time);
                Boolean finalPlayerPlaying = playerPlaying;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("test", "syncJoinBack");
                        playerseek = true;
                        player.seekTo(finalSong, finalTime);
                        if (finalPlayerPlaying) {
                            player.setPlayWhenReady(true);
                        }
                    }
                });
            }
        }
    };

    public ArrayList<String> filterNames(ArrayList<String> theList){
        Set<String> s = new LinkedHashSet<>(theList);
        ArrayList<String> newList = new ArrayList<>();
        newList.addAll(s);
        return newList;
    }

    public MutableLiveData<ArrayList<String>> getUsernamesLive(){
        return usernamesLive;
    }


    private void connectToSocketIO() {
        JSONObject channelName = new JSONObject();
        socketIOClient.mSocket = socketIOClient.getSocket();
        socketIOClient.mSocket.on("new_playerstate",onNewPlayerstate);
        socketIOClient.mSocket.on("new_activeUsers",onNewActiveUsers);
        socketIOClient.mSocket.on("new_syncOnJoin",onNewSyncOnJoin);
        socketIOClient.mSocket.connect();
        try{
            channelName.put("channel", room);
        }catch(JSONException e){
            e.printStackTrace();
        }
        socketIOClient.mSocket.emit("join", channelName);
    }
}
