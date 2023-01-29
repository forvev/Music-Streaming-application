package com.example.musicfun.fragment.mymusic;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;


import com.example.musicfun.R;
import com.example.musicfun.adapter.mymusic.ChoosePlaylistAdapter;
import com.example.musicfun.adapter.search.SearchPlaylistAdapter;
import com.example.musicfun.databinding.FragmentChoosePlaylistBinding;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.interfaces.SelectPlaylistInterface;
import com.example.musicfun.viewmodel.mymusic.PlaylistViewModel;

import java.util.ArrayList;

/**
 * Once we click on the specific song that we want to add a song to the playlist this fragment will be displayed.
 * We can use already created playlist or create a new one
 */
public class ChoosePlaylistFragment extends Fragment {

    private FragmentChoosePlaylistBinding binding;
    private PlaylistViewModel viewModel;
    private ListView listView;
    private LinearLayout create_new_playlist;
    private ChoosePlaylistAdapter adapter;
    private Button save;
    private Button cancel;
    private int selected_pos;
    private boolean hasChosen;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(PlaylistViewModel.class);
        binding = FragmentChoosePlaylistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sp = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        username = sp.getString("name", "");

        create_new_playlist = binding.createNewPlaylist;
        create_new_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPlaylist();
            }
        });
        viewModel.getAllOwnedPlaylists();
        viewModel.getM_playlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Playlist>>(){
            @Override
            public void onChanged(ArrayList<Playlist> playlists) {
                if(!playlists.isEmpty()){
                    listView = binding.chooseOnePlaylist;
                    playlists = sortPlaylist(playlists);
                    adapter = new ChoosePlaylistAdapter(getContext(), playlists, selectPlaylistInterface);
                    listView.setAdapter(adapter);
                    save = binding.savePlaylist;
                    cancel = binding.cancelSelection;
                    ArrayList<Playlist> finalPlaylists = playlists;
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Limitation: Only one item can be transfered back to the previous fragment
                            if(hasChosen){
                                Toast.makeText(getContext(), getString(R.string.added_to) + " " + finalPlaylists.get(selected_pos).getPlaylist_name(), Toast.LENGTH_SHORT).show();
                                NavController navController = NavHostFragment.findNavController(ChoosePlaylistFragment.this);
                                navController.getPreviousBackStackEntry().getSavedStateHandle().set("key", finalPlaylists.get(selected_pos).getPlaylist_id());
                                navController.popBackStack();
                            }
                            else{
                                Toast.makeText(getContext(), "Please choose a playlist!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NavController navController = NavHostFragment.findNavController(ChoosePlaylistFragment.this);
                            navController.popBackStack();
                        }
                    });
                }
            }
        });

    }

//    Sort the playlist so that the first item is Default playlist, then all personal playlists, then shared playlists which this owner owns, then the rest of the shared playlists.
    private ArrayList<Playlist> sortPlaylist(ArrayList<Playlist> playlists){
        ArrayList<Playlist> def = new ArrayList<>();
        ArrayList<Playlist> personal = new ArrayList<>();
        ArrayList<Playlist> owner = new ArrayList<>();
        ArrayList<Playlist> rest = new ArrayList<>();
        for (Playlist p : playlists){
            if (p.isDefault()){
                def.add(p);
            }
            else if (!p.isShared()){
                personal.add(p);
            }
            else if (p.isShared() && p.getOwner().equals(username)){
                owner.add(p);
            }
            else{
                rest.add(p);
            }
        }
        def.addAll(personal);
        def.addAll(owner);
        def.addAll(rest);
        return def;
    }

    SelectPlaylistInterface selectPlaylistInterface = new SelectPlaylistInterface() {
        public void setSelectedPlaylistIndex(int pos){
            hasChosen = true;
            selected_pos = pos;
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
                    nameEt.setError(getString(R.string.playlist_need_name));
                }
                else{
                    viewModel.createPlaylists(playlistName);
                    dialog.dismiss();

                }
            }
        });
    }
}
