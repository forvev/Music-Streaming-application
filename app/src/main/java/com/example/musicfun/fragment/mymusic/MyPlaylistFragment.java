package com.example.musicfun.fragment.mymusic;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

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

import com.example.musicfun.adapter.mymusic.SongListAdapter;
import com.example.musicfun.adapter.search.SearchSonglistAdapter;
import com.example.musicfun.databinding.FragmentSongsBinding;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.interfaces.SonglistMenuClick;
import com.example.musicfun.viewmodel.mymusic.SonglistViewModel;

import java.util.ArrayList;

public class MyPlaylistFragment extends Fragment {

    private FragmentSongsBinding binding;
    private SonglistViewModel viewModel;
    private ListView listView;
    private SongListAdapter adapter;
    private ArrayList<Songs> songList = new ArrayList<>();
    private String selected_playlist_id;
    private String song_id;
    private SearchView searchView;
    private SearchSonglistAdapter searchResultAdapter;
    private ListView searchResult;
    private ImageButton playAllSongs;
    private ImageButton shuffle;
    private PassDataInterface passData;


    private SonglistMenuClick songlistMenuClick = new SonglistMenuClick() {
        @Override
        public void removeFromPlaylist(int position) {
            //Toast.makeText(getContext(), "remove from playlist", Toast.LENGTH_SHORT).show();
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
        binding = FragmentSongsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

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
                adapter = new SongListAdapter(getActivity(), songs, songlistMenuClick);
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
//                TODO: enable all three modes in the app
                passData.playSong(viewModel.getM_songlist().getValue(), 2, false);
            }
        });
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "shuffle button is clicked", Toast.LENGTH_SHORT).show();
                passData.playSong(viewModel.getM_songlist().getValue(), 1, true);
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
                    //Toast.makeText(getContext(), "add this song to playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        search function
        searchView = binding.searchSongResult;
        searchView.setOnQueryTextFocusChangeListener(searchClickListner);
        searchView.setQueryHint("Find song in this playlist");
        searchResult = binding.searchSonglistResult;
    }

    private View.OnFocusChangeListener searchClickListner = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (searchResult != null) {
                searchResult.setVisibility(View.VISIBLE);
            }

            binding.setting.setVisibility(View.GONE);
            binding.cancel.setVisibility(View.VISIBLE);
            // cancel the search
            binding.cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeKeyboard(view);
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    listView.setVisibility(View.VISIBLE);
                    searchResult.setVisibility(View.INVISIBLE);
                    binding.setting.setVisibility(View.VISIBLE);
                    binding.cancel.setVisibility(View.GONE);
                    playAllSongs.setVisibility(View.VISIBLE);
                    shuffle.setVisibility(View.VISIBLE);
                }
            });
            viewModel.getSongsFromPlaylist(selected_playlist_id);
            playAllSongs.setVisibility(View.GONE);
            shuffle.setVisibility(View.GONE);
            listView.setVisibility(View.INVISIBLE);

            // pass results to ListViewAdapter Class
            viewModel.getM_searchResult().observe(getViewLifecycleOwner(), new Observer<ArrayList<Songs>>() {
                @Override
                public void onChanged(@Nullable final ArrayList<Songs> newName) {
                    searchResultAdapter = new SearchSonglistAdapter(getActivity(), newName);
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
                            viewModel.searchSongByName(text, selected_playlist_id);
                            return true;
                        }
                    });
                }
            });
            searchResult.setOnTouchListener(new View.OnTouchListener() {
                // hide soft keyboard if a user is scrolling the result list
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    closeKeyboard(v);
                    return false;
                }
            });
            searchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    closeKeyboard(view);
                    Playlist p = (Playlist) searchResult.getItemAtPosition(i);
                    String id = p.getPlaylist_id();
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                    searchResult.setVisibility(View.INVISIBLE);
                    listView.setVisibility(View.VISIBLE);
                    playAllSongs.setVisibility((View.VISIBLE));
                    shuffle.setVisibility((View.VISIBLE));
                    binding.setting.setVisibility(View.VISIBLE);
                    binding.cancel.setVisibility(View.GONE);
                    NavDirections action = MyMusicFragmentDirections.actionMyMusicToMyPlaylistFragment(id);
                    Navigation.findNavController(getView()).navigate(action);
                }
            });
        }
    };
    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
