package com.example.musicfun.fragment.mymusic;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.musicfun.R;
import com.example.musicfun.adapter.mymusic.PlaylistAdapter;
import com.example.musicfun.adapter.search.SearchPlaylistAdapter;
import com.example.musicfun.databinding.FragmentMymusicBinding;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.FragmentTransfer;
import com.example.musicfun.interfaces.PlaylistMenuClick;
import com.example.musicfun.viewmodel.mymusic.PlaylistViewModel;

import java.util.ArrayList;

public class MyMusicFragment extends Fragment {
    private FragmentMymusicBinding binding;
    private PlaylistViewModel viewModel;
    private ListView listView;
    private PlaylistAdapter playlistAdapter;
    private ImageButton add_playlist;

    private ListView searchResult;
    private SearchView searchView;
    private SearchPlaylistAdapter searchResultAdapter;

    private PlaylistMenuClick playlistMenuClick = new PlaylistMenuClick(){
        @Override
        public void renamePlaylist(String playlistName, int position) {
            // send playlist_id and new name to server
            // check whether the playlist names are duplicated
            viewModel.renamePlaylist(playlistName, position);
            Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void setDefaultPlaylist(int position, boolean isDefault) {
            // send playlist_id to server as default playlist
            if(!isDefault){
                if(position == 0){
                    Toast.makeText(getContext(), "You need to have a default playlist!", Toast.LENGTH_SHORT).show();
                }
                else{
                    viewModel.setAsDefault(0);
                }
            }
            else{
                viewModel.setAsDefault(position);
            }
            Toast.makeText(getContext(), "setDefaultPlaylist", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void deletePlaylist(int position) {
            // remove this playlist from server
            viewModel.deletePlaylist(position);
            Toast.makeText(getContext(), "remove playlist", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void share(int position) {
            // send this playlist to friends
            Toast.makeText(getContext(), "share playlist", Toast.LENGTH_SHORT).show();
        }
    };
    private FragmentTransfer fragmentTransfer = new FragmentTransfer(){
        @Override
        public void transferFragment(String selected_playlist_id) {
            NavDirections action = MyMusicFragmentDirections.actionMyMusicToMyPlaylistFragment(selected_playlist_id);
            Navigation.findNavController(getView()).navigate(action);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        binding = FragmentMymusicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_playlist = binding.addPlaylist;
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPlaylist();
            }
        });
        viewModel.getAllPlaylists();
        viewModel.getM_playlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Playlist>>(){
            @Override
            public void onChanged(ArrayList<Playlist> playlists) {
                listView = binding.playlist;
                playlistAdapter = new PlaylistAdapter(getContext(), playlists, playlistMenuClick, fragmentTransfer);
                listView.setAdapter(playlistAdapter);
            }
        });
        // set searchView and search result listview
        searchView = binding.searchPlaylist;
        searchView.setOnQueryTextFocusChangeListener(searchClickListner);
        searchView.setQueryHint("Find playlist");
        searchResult = binding.searchPlaylistResult;
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
                    add_playlist.setVisibility(View.VISIBLE);
                    binding.playlist.setVisibility(View.VISIBLE);
                }
            });
            add_playlist.setVisibility(View.GONE);
            binding.playlist.setVisibility(View.GONE);
            listView.setVisibility(View.INVISIBLE);
            // pass results to ListViewAdapter Class
            viewModel.getM_searchResult().observe(getViewLifecycleOwner(), new Observer<ArrayList<Playlist>>() {
                @Override
                public void onChanged(@Nullable final ArrayList<Playlist> newName) {
                    searchResultAdapter = new SearchPlaylistAdapter(getActivity(), newName);
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
                            viewModel.searchPlaylistByName(text);
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
                    add_playlist.setVisibility((View.VISIBLE));
                    binding.setting.setVisibility(View.VISIBLE);
                    binding.cancel.setVisibility(View.GONE);
                    NavDirections action = MyMusicFragmentDirections.actionMyMusicToMyPlaylistFragment(id);
                    Navigation.findNavController(getView()).navigate(action);
                }
            });
        }
    };

    private void createPlaylist(){
        final Dialog dialog = new Dialog(getActivity());
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_create_playlist);
        dialog.show();

        //Initializing the views of the dialog.
        Button submitButton = dialog.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText nameEt = dialog.findViewById(R.id.name_et);
                String playlistName = nameEt.getText().toString();
                // send the input playlist name to the server
                if(TextUtils.isEmpty(playlistName)) {
                    nameEt.setError("Please give your playlist a name!");
                }
                else{
                    Toast.makeText(getContext(), "playlist saved", Toast.LENGTH_SHORT).show();
                    viewModel.createPlaylists(playlistName);
                    dialog.dismiss();

                }
            }
        });
    }

    private void closeKeyboard(View view) {
        // this will give us the view which is currently focus in this layout
        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
