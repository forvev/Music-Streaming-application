package com.example.musicfun.activity;

import static android.app.NotificationManager.IMPORTANCE_HIGH;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicfun.R;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.viewmodel.MainActivityViewModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.analytics.PlaybackStatsListener;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicbannerService extends Service {

    //member
    private final IBinder serviceBinder = new ServiceBinder();
    //broadcast to main activity
    static final public String COPA_RESULT = "com.example.musicfun.activity.MusicbannerService";
    //player
    public ExoPlayer player;
    private boolean startAutoPlay;
    private int startItemIndex;
    private long startPosition;

    private SharedPreferences sp;

    PlayerNotificationManager notificationManager;
    private List<MediaItem> mediaItems = new ArrayList<>();
    private List<Songs> songInfo = new ArrayList<>();
    LocalBroadcastManager broadcaster;
    MainActivityViewModel viewModel;

    public void setSongInfo (List<Songs> songInfo){
        this.songInfo = songInfo;
    }

    //class binder for clients
    public class ServiceBinder extends Binder {
        public MusicbannerService getMusicbannerService(){
            return MusicbannerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
        //assign variables
        player = new ExoPlayer.Builder(getApplicationContext()).build();
        viewModel = new MainActivityViewModel(getApplication());

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
                    if(playbackStats.getTotalPlayTimeMs() > 1000 && player != null){
                        viewModel.sendListenHistory(player.getCurrentMediaItem().mediaMetadata.description.toString());
                    }
                    // TODO: add listen history UI to setting activity
//                    System.out.println("playbackStats.getTotalPlayTimeMs() = " + playbackStats.getTotalPlayTimeMs());
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
        public void onPlaybackStateChanged(@Player.State int playbackState) {
            if (playbackState == Player.STATE_ENDED) {
            }
        }

        @Override
        public void onPlayerError(PlaybackException error) {
            if (error.errorCode == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
                player.seekToDefaultPosition();
                player.prepare();
            } else {
            }
        }

        @Override
        public void onMediaItemTransition(@Nullable MediaItem mediaItem, @Player.MediaItemTransitionReason int reason) {
            // detects whether a song in the playlist is finished
            Player.Listener.super.onMediaItemTransition(mediaItem, reason);
            if(mediaItem == null){
                return;
            }
            String title = mediaItem.mediaMetadata.title.toString();
            String artist = mediaItem.mediaMetadata.artist.toString();
            sendSongInfo(title, artist);

            // TODO: update lyrics file
        }

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
    protected void sendSongInfo(String title, String artist) {
        Intent intent1 = new Intent(COPA_RESULT);
        intent1.putExtra("title", title);
        intent1.putExtra("artist", artist);
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
            List<Songs> restOfPlaylist = songInfo.subList(startItemIndex, songInfo.size());
            Gson gson = new Gson();
            String json = gson.toJson(restOfPlaylist);
            sp.edit().putString("saved_playlist", json).apply();
        }
    }

}