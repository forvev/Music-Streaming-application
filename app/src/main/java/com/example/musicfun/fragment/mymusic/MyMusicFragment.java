package com.example.musicfun.fragment.mymusic;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.musicfun.R;
import com.example.musicfun.activity.MainActivity;
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
    private ImageView add_playlist;

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
        }

        @Override
        public void deletePlaylist(int position) {
            // remove this playlist from server
            viewModel.deletePlaylist(position);
        }

        @Override
        public void share(int position) {
            // send this playlist to friends
            Toast.makeText(getContext(), "This playlist is now shared!", Toast.LENGTH_SHORT).show();
            viewModel.setAsShare(position);
        }
    };

    private FragmentTransfer fragmentTransfer = new FragmentTransfer(){
        @Override
        public void transferFragment(String selected_playlist_id) {
            ((MainActivity)getActivity()).setPlaylistId(selected_playlist_id);
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
    }

    private void createPlaylist(){
        final Dialog dialog = new Dialog(getActivity());
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.dialog_create_playlist);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

}
