package com.example.musicfun.fragment;

import static com.example.musicfun.activity.MusicbannerService.COPA_RESULT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.musicfun.R;
import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.activity.MusicbannerService;
import com.example.musicfun.databinding.FragmentLyricsBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ui.StyledPlayerControlView;

public class LyricsFragment extends Fragment {

    private FragmentLyricsBinding binding;
    private MusicbannerService service;
    protected @Nullable ExoPlayer player;
    private BroadcastReceiver broadcastReceiver;

    private StyledPlayerControlView controlView;
    private TextView tv_title;
    private TextView tv_artist;
    private ImageView back;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentLyricsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlView = binding.playerView;
        controlView.setShowTimeoutMs(0);
        service = ((MainActivity)getActivity()).getService();
        player = service.player;
        controlView.setPlayer(player);

//      Initialize the views
        tv_title = getView().findViewById(R.id.styled_player_song_name);
        tv_artist = getView().findViewById(R.id.styled_player_artist);
        Bundle arguments = getArguments();
        String title = arguments.getString("title");
        tv_title.setText(title);
        String artist = arguments.getString("artist");
        tv_artist.setText(artist);
        back = binding.scaleDownToBanner;
        back.setOnClickListener(backToPreviousFragment);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String title = intent.getStringExtra("title");
                String artist = intent.getStringExtra("artist");
                tv_title.setText(title);
                tv_artist.setText(artist);
            }
        };
    }

    private View.OnClickListener backToPreviousFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Hide the layout. The fragment is not paused!!!!
            FrameLayout frameLayout = getActivity().findViewById(R.id.fl_lyrics);
            frameLayout.setVisibility(View.GONE);
        }
    };


    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver((broadcastReceiver), new IntentFilter(COPA_RESULT));
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }
}
