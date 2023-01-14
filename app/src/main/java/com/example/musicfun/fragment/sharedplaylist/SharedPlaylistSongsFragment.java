package com.example.musicfun.fragment.sharedplaylist;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
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
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.LyricsActivity;
import com.example.musicfun.adapter.mymusic.SongListAdapter;
import com.example.musicfun.databinding.FragmentSharedPlaylistSongsBinding;
import com.example.musicfun.databinding.FragmentSongsBinding;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.fragment.mymusic.MyPlaylistFragment;
import com.example.musicfun.fragment.mymusic.MyPlaylistFragmentArgs;
import com.example.musicfun.fragment.mymusic.MyPlaylistFragmentDirections;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.SonglistMenuClick;
import com.example.musicfun.viewmodel.mymusic.SonglistViewModel;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SharedPlaylistSongsFragment extends Fragment {

    private FragmentSharedPlaylistSongsBinding binding;
    private SonglistViewModel viewModel;
    private ListView listView;
    private SongListAdapter adapter;
//    private ArrayList<Songs> songList = new ArrayList<>();
    private String selected_playlist_id;
    private String song_id;
    private PassDataInterface passData;
    private TextView tv_listenTogether;

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
        public void share(int position) {
            Toast.makeText(getContext(), "share this song", Toast.LENGTH_SHORT).show();
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(SonglistViewModel.class);
        binding = FragmentSharedPlaylistSongsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_listenTogether = binding.listenTogether;
        selected_playlist_id = SharedPlaylistSongsFragmentArgs.fromBundle(getArguments()).getSelectedSharedId();
//        fetch songs from this specific playlist
        viewModel.getSongsFromPlaylist(selected_playlist_id);
        viewModel.getM_songlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Songs>>(){
            @Override
            public void onChanged(ArrayList<Songs> songs) {
                listView = binding.songlist;
                adapter = new SongListAdapter(getActivity(), songs, songlistMenuClick);
                listView.setAdapter(adapter);
                if (songs.size() != 0){
                    binding.empty.setVisibility(View.GONE);
                    tv_listenTogether.setVisibility(View.VISIBLE);
                    tv_listenTogether.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            TODO: get current position and current item index from server
                            passData.seek(songs, 0, 0);

                            Intent intent = new Intent(getActivity(), LyricsActivity.class);
//                            Gson gson = new Gson();
//                            String json = gson.toJson(songs);
                            intent.putExtra("title", songs.get(0).getSongName());
                            intent.putExtra("artist", songs.get(0).getArtist());
                            intent.putExtra("listenTogether", true);
//                            intent.putExtra("playlist", json);
                            startActivity(intent);
                        }
                    });
                }
                else{
                    tv_listenTogether.setVisibility(View.GONE);
                    binding.empty.setVisibility(View.VISIBLE);
                }
            }
        });

//         listen whether there is selected playlist id popped back from ChoosePlaylistFragment
        NavController navController = NavHostFragment.findNavController(SharedPlaylistSongsFragment.this);
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
