package com.example.musicfun.activity;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import static com.google.android.exoplayer2.Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.viewmodel.MainActivityViewModel;
import com.example.musicfun.viewmodel.mymusic.SonglistViewModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.PlaybackStatsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.source.ShuffleOrder;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * MusicbannerService is the only place to create new Exoplayer. Basic player listeners are implemented,
*          including send out broadcasts when player starts the next song,
*         and determine the seed of shuffle order, if shuffle is enabled.
*  MusicbannerService runs above all activities. If a java class wants to have control to the player, it has to be bound to this service first.
 */
public class MusicbannerService extends LifecycleService {

    //member
    private final IBinder serviceBinder = new ServiceBinder();
    //broadcast to main activity
    static final public String COPA_RESULT = "com.example.musicfun.activity.MusicbannerService";
    //player
    public ExoPlayer player;
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;
    private ArrayList<Integer> preOrder = new ArrayList<>();

    private SharedPreferences sp;

    PlayerNotificationManager notificationManager;
    private List<MediaItem> mediaItems = new ArrayList<>();
    private List<Songs> songInfo = new ArrayList<>();
    LocalBroadcastManager broadcaster;
    MainActivityViewModel viewModel;
    SonglistViewModel songlistViewModel;
    private boolean isSession;
    private String current_playlist_id;
    private int numberOfSongs;
    private String temp_playlist_id;

    public boolean isSession() {
        return isSession;
    }

    public void setSession(boolean session, String current_playlist_id) {
        this.isSession = session;
        this.current_playlist_id = current_playlist_id;
    }

    public void setSongInfo (List<Songs> songInfo){
        this.songInfo = songInfo;
        this.numberOfSongs = songInfo.size();
    }

    public List<Songs> getSongInfo(){
        return songInfo;
    }

    //class binder for clients
    public class ServiceBinder extends Binder {
        public MusicbannerService getMusicbannerService(){
            return MusicbannerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return serviceBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("onCreate in Service!!!");
        broadcaster = LocalBroadcastManager.getInstance(this);
        //assign variables
        player = new ExoPlayer.Builder(getApplicationContext()).build();
        viewModel = new MainActivityViewModel(getApplication());
        songlistViewModel = new SonglistViewModel(getApplication());
        //audio focus attributes
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
                .build();
        player.setAudioAttributes(audioAttributes, true);
        player.addListener(new PlayerEventListener());
        player.addAnalyticsListener(new PlaybackStatsListener(false, (eventTime, playbackStats) -> {
                    // Analytics data for the session started at `eventTime` is ready
                    // Songs which are played more than 10 seconds are considered as listen history and will be sent to database

                    if(playbackStats.getTotalPlayTimeMs() > 1000 && player != null && sp.getInt("logged", 999) == 1){
                        System.out.println("current media item = " + temp_playlist_id);
                        viewModel.sendListenHistory(temp_playlist_id);
                    }
                }));

        sp = getSharedPreferences("login",MODE_PRIVATE);

        //notification manager
        final String channelId = getResources().getString(R.string.app_name);
        final int notificationId = 111111;
        notificationManager = new PlayerNotificationManager.Builder(this, notificationId, channelId)
                .setNotificationListener(notificationListener)
                .setMediaDescriptionAdapter(descriptionAdapter)
                .setChannelImportance(IMPORTANCE_HIGH)
                .setSmallIconResourceId(R.drawable.ic_purple)
                .setChannelDescriptionResourceId(R.string.app_name)
                .setNextActionIconResourceId(R.drawable.ic_baseline_skip_next_24)
                .setPreviousActionIconResourceId(R.drawable.ic_baseline_skip_previous_24)
                .setPauseActionIconResourceId(R.drawable.ic_baseline_pause_24)
                .setPlayActionIconResourceId(R.drawable.ic_baseline_play_arrow_24)
                .setChannelNameResourceId(R.string.app_name)
                .build();

        //set player to notification manager
        notificationManager.setPlayer(player);
        notificationManager.setPriority(NotificationCompat.PRIORITY_MAX);
        notificationManager.setUseRewindAction(false);
        notificationManager.setUseFastForwardAction(false);

    }

    @Override
    public void onDestroy(){
        System.out.println("onDestroy in Service!!!");
        if(player.isPlaying()){
            player.stop();
        }
        notificationManager.setPlayer(null);
        releasePlayer();
        stopForeground(true);
        stopSelf();
        super.onDestroy();
    }

    //notification listener
    PlayerNotificationManager.NotificationListener notificationListener = new PlayerNotificationManager.NotificationListener() {
        @Override
        public void onNotificationCancelled(int notificationId, boolean dismissedByUser) {
            PlayerNotificationManager.NotificationListener.super.onNotificationCancelled(notificationId, dismissedByUser);
            stopForeground(true);
            if (player.isPlaying()){
                player.pause();
            }
        }

        @Override
        public void onNotificationPosted(int notificationId, Notification notification, boolean ongoing) {
            PlayerNotificationManager.NotificationListener.super.onNotificationPosted(notificationId, notification, ongoing);
            startForeground(notificationId, notification);
        }
    };

    //notification description adapter
    PlayerNotificationManager.MediaDescriptionAdapter descriptionAdapter = new PlayerNotificationManager.MediaDescriptionAdapter() {
        @Override
        public CharSequence getCurrentContentTitle(Player player) {
            CharSequence currentTitle = "";
            if(player.getCurrentMediaItem() != null){
                currentTitle= player.getCurrentMediaItem().mediaMetadata.title;
            }
            return currentTitle;
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            //intent to open the app when clicked
            Intent openAppIntent = new Intent((getApplicationContext()), MainActivity.class);
            return PendingIntent.getActivity(getApplicationContext(), 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        }

        @Nullable
        @Override
        public CharSequence getCurrentContentText(Player player) {
            return null;
        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
            return null;
        }
    };

    private class PlayerEventListener implements Player.Listener {

        @Override
        public void onPlayerError(PlaybackException error) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player.seekToDefaultPosition();
                player.prepare();
            }
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, @Player.MediaItemTransitionReason int reason) {
            // detects whether a song in the playlist is finished
            Player.Listener.super.onMediaItemTransition(mediaItem, reason);
            if(mediaItem == null){
                return;
            }
            temp_playlist_id = player.getCurrentMediaItem().mediaMetadata.description.toString();
            String id = mediaItem.mediaMetadata.description.toString();
            String coverUrl = "http://10.0.2.2:3000/images/" + id + ".jpg";
            String title = mediaItem.mediaMetadata.title.toString();
            String artist = mediaItem.mediaMetadata.artist.toString();
            sendSongInfo(title, artist, coverUrl);

            if (isSession){
                songlistViewModel.getSongsFromPlaylist(current_playlist_id);
                songlistViewModel.getM_songlist().observe(MusicbannerService.this, new Observer<ArrayList<Songs>>() {
                    @Override
                    public void onChanged(ArrayList<Songs> songs) {
                        if(!songs.isEmpty() && numberOfSongs != songs.size()){
                            player.setShuffleModeEnabled(false);
                            numberOfSongs = songs.size();
                            updateSharedPlaylist(songs);
                        }
                    }
                });
            }
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled){
            if(shuffleModeEnabled){
                preOrder.clear();
                for (int i = 0; i < player.getMediaItemCount(); i++){
                    preOrder.add(i);
                }
                Collections.shuffle(preOrder, new Random(player.getMediaItemCount()));
                int [] array_order = new int[player.getMediaItemCount()];
                for (int i = 0; i < preOrder.size(); i++){
                    array_order[i] = preOrder.get(i);
                }
                ShuffleOrder.DefaultShuffleOrder order = new ShuffleOrder.DefaultShuffleOrder(array_order, 1);
                player.setShuffleOrder(order);
            }
        }
    }

    private void updateSharedPlaylist(List<Songs> playlist){
        mediaItems.clear();
        numberOfSongs = playlist.size();
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
        setSongInfo(playlist);
        player.setMediaItems(mediaItems, false);
    }

    public ArrayList<Integer> getList_order (){
        return preOrder;
    }

    public void setPlaylist(List<MediaItem> mediaItems, int startItemIndex, long startPosition, boolean startAutoPlay){
        boolean haveStartPosition = startItemIndex != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(startItemIndex, startPosition);
        }
        player.setMediaItems(mediaItems, /* resetPosition= */ !haveStartPosition);
        player.prepare();
        player.setPlayWhenReady(startAutoPlay);
    }

    // send back to main activity to update music banner UI
    protected void sendSongInfo(String title, String artist, String coverUrl) {
        Intent intent1 = new Intent(COPA_RESULT);
        intent1.putExtra("title", title);
        intent1.putExtra("artist", artist);
        intent1.putExtra("coverUrl", coverUrl);
        broadcaster.sendBroadcast(intent1);
    }

    public void releasePlayer() {
        if (player != null) {
            updateStartPosition();
            player.release();
            player = null;
            mediaItems = Collections.emptyList();
        }
    }

    private void updateStartPosition() {
        if (player != null) {
            startAutoPlay = false;
            startItemIndex = player.getCurrentMediaItemIndex();
            startPosition = Math.max(0, player.getContentPosition());
            sp.edit().putInt("startItemIndex", startItemIndex).apply();
            sp.edit().putLong("startPosition", startPosition).apply();
            Gson gson = new Gson();
            String json = gson.toJson(songInfo);
            sp.edit().putString("saved_playlist", json).apply();
        }
    }


}