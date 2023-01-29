package com.example.musicfun.fragment.sharedplaylist;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.R;
import com.example.musicfun.activity.LyricsActivity;
import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.adapter.SharedPlaylist.SharedPlaylistAdapter;
import com.example.musicfun.databinding.FragmentMymusicBinding;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.FragmentTransfer;
import com.example.musicfun.interfaces.PlaylistMenuClick;
import com.example.musicfun.ui.friends.FriendsFragment;
import com.example.musicfun.ui.friends.Friends_friend_Fragment;
import com.example.musicfun.ui.friends.List_of_friends_fragment;
import com.example.musicfun.viewmodel.mymusic.PlaylistViewModel;

import java.util.ArrayList;
import java.util.List;

/**
The implementation of shared playlist is included here. Once the fragment is opened the list of shared playlist will be displayed.

 **/
public class SharedPlaylistFragment extends Fragment {

    private FragmentMymusicBinding binding;
    private PlaylistViewModel viewModel;
    private ListView listView;
    private SharedPlaylistAdapter playlistAdapter;
    private ImageView add_playlist;
    private String username;


    private PlaylistMenuClick playlistMenuClick = new PlaylistMenuClick(){
        @Override
        public void renamePlaylist(String playlistName, int position) {
            // send playlist_id and new name to server
            // check whether the playlist names are duplicated
            viewModel.renamePlaylist(playlistName, position);
        }

        @Override
        public void setDefaultPlaylist(int position, boolean isDefault) {
//          Do nothing for shared playlists
        }

        @Override
        public void deletePlaylist(int position) {
            // remove this playlist from server
            viewModel.deletePlaylist(position);
        }

        @Override
        public void share(List<Playlist> my_playlists, int position) {
            //Using navigation approach move to another fragment
            NavDirections action = SharedPlaylistFragmentDirections.actionFriendsSharedPlaylistToListOfFriendsFragment(my_playlists.get(position).getPlaylist_id());
            Navigation.findNavController(getView()).navigate(action);
        }

    };
    private FragmentTransfer fragmentTransfer = new FragmentTransfer(){
        @Override
        public void transferFragment(String selected_shared_id, boolean isOwner) {
            //selected_shared_id -> we pass by the id selected playlist to another fragment
            ((MainActivity)getActivity()).setPlaylistId(selected_shared_id);
            NavDirections action = SharedPlaylistFragmentDirections.actionSharedPlaylistFragmentToSharedPlaylistSongsFragment(selected_shared_id, isOwner);
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
        SharedPreferences sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        username = sp.getString("name", "");
        add_playlist = binding.addPlaylist;
        add_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPlaylist();
            }
        });
        viewModel.getSharedPlaylists();
        viewModel.getM_playlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Playlist>>(){
            @Override
            public void onChanged(ArrayList<Playlist> playlists) {
                if(!playlists.isEmpty()){
                    playlists = sortPlaylist(playlists);
                    listView = binding.playlist;
                    playlistAdapter = new SharedPlaylistAdapter(getContext(), playlists, playlistMenuClick, fragmentTransfer);
                    listView.setAdapter(playlistAdapter);
                }
            }
        });
    }

//    Sort the playlists so that the ones this user owns are shown first.
    private ArrayList<Playlist> sortPlaylist (ArrayList<Playlist> playlists){
        ArrayList<Playlist> isOwner = new ArrayList<>();
        ArrayList<Playlist> rest = new ArrayList<>();
        for (Playlist p : playlists){
            if (p.getOwner().equals(username)){
                isOwner.add(p);
            }
            else{
                rest.add(p);
            }
        }
        isOwner.addAll(rest);
        return isOwner;
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
                    nameEt.setError(getString(R.string.need_name));
                }
                else{
                    viewModel.createSharedPlaylist(playlistName);
                    dialog.dismiss();

                }
            }
        });
    }
}
