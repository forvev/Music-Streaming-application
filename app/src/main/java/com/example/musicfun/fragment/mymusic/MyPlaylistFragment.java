package com.example.musicfun.fragment.mymusic;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.MusicbannerService;
import com.example.musicfun.adapter.mymusic.SongListAdapter;
import com.example.musicfun.databinding.FragmentSongsBinding;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.SonglistMenuClick;
import com.example.musicfun.viewmodel.mymusic.SonglistViewModel;
import com.google.android.exoplayer2.ExoPlayer;

import java.util.ArrayList;

public class MyPlaylistFragment extends Fragment {

    private FragmentSongsBinding binding;
    private SonglistViewModel viewModel;
    private ListView listView;
    private SongListAdapter adapter;
    private ArrayList<Songs> songList = new ArrayList<>();
    private String selected_playlist_id;
    private String song_id;
    private ImageView playAllSongs;
    private ImageView shuffle;
    private boolean isShuffle;
    private PassDataInterface passData;
//    service relevant
    private boolean isBound;
    private MusicbannerService service;
    private ExoPlayer player;

    private SonglistMenuClick songlistMenuClick = new SonglistMenuClick() {
        @Override
        public void removeFromPlaylist(int position) {
            viewModel.deleteSongsFromPlaylist(selected_playlist_id, position);
        }

        @Override
        public void addToPlaylist(String songId) {
            NavDirections action = MyPlaylistFragmentDirections.actionMyPlaylistFragmentToChooseOnePlaylist();
            Navigation.findNavController(getView()).navigate(action);
            song_id = songId;
        }

        @Override
        public void addToDefault(String position) {

        }
    };

    ServiceConnection playerServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            isBound = true;
            MusicbannerService.ServiceBinder binder = (MusicbannerService.ServiceBinder) iBinder;
            service = binder.getMusicbannerService();
            player = service.player;
            if (player.getShuffleModeEnabled()){
                isShuffle = true;
                shuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
            }
            else{
                isShuffle = false;
                shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        Intent musicbannerServiceIntent = new Intent(getActivity(), MusicbannerService.class);
        getActivity().bindService(musicbannerServiceIntent, playerServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        if (isBound){
            doUnbindService();
            isBound = false;
        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        if (isBound){
            doUnbindService();
            isBound = false;
        }
        doUnbindService();
        super.onDestroy();
    }

    private void doUnbindService(){
        if (isBound){
            getActivity().unbindService(playerServiceConnection);
            isBound = false;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(SonglistViewModel.class);
        binding = FragmentSongsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        selected_playlist_id = MyPlaylistFragmentArgs.fromBundle(getArguments()).getSelectedPlaylistId();
//        fetch songs from this specific playlist
        viewModel.getSongsFromPlaylist(selected_playlist_id);
        viewModel.getM_songlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Songs>>(){
            @Override
            public void onChanged(ArrayList<Songs> songs) {
                listView = binding.songlist;
                adapter = new SongListAdapter(getActivity(), songs, songlistMenuClick, isShuffle);
                listView.setAdapter(adapter);
                if (songs.size() != 0){
                    binding.empty.setVisibility(View.GONE);
                }
                else{
                    binding.empty.setVisibility(View.VISIBLE);
                }
            }
        });

//        play all songs in this playlist
        playAllSongs = binding.playAllSongs;
        shuffle = binding.shuffle;
        playAllSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                repeatMode: Player.REPEAT_MODE_OFF = 0, Player.REPEAT_MODE_ONE = 1, Player.REPEAT_MODE_ALL = 2
                passData.playSong(viewModel.getM_songlist().getValue(), 2);
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShuffle){
                    shuffle.setImageResource(R.drawable.ic_baseline_shuffle_on_24);
                    player.setShuffleModeEnabled(true);
                    isShuffle = true;
                }
                else{
                    shuffle.setImageResource(R.drawable.ic_baseline_shuffle_24);
                    player.setShuffleModeEnabled(false);
                    isShuffle = false;
                }
            }
        });

//         listen whether there is selected playlist id popped back from ChoosePlaylistFragment
        NavController navController = NavHostFragment.findNavController(MyPlaylistFragment.this);
        MutableLiveData<String> liveData = navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("key");
        liveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String playlist_position) {
                if(playlist_position != null){
                    viewModel.addSongToPlaylist(playlist_position, song_id);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            passData = (PassDataInterface) getActivity();
        }catch(ClassCastException e){
            Log.e(TAG, "onAttach: ClassCast WRONG " + e.getMessage());
        }
    }



}
