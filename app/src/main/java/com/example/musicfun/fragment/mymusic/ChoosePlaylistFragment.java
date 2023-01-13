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
import com.example.musicfun.viewmodel.mymusic.PlaylistViewModel;

import java.util.ArrayList;

public class ChoosePlaylistFragment extends Fragment {

    private FragmentChoosePlaylistBinding binding;
    private PlaylistViewModel viewModel;
    private ListView listView;
    private LinearLayout create_new_playlist;
    private ArrayAdapter<String> arrayAdapter;
    private ChoosePlaylistAdapter adapter;

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
                listView = binding.chooseOnePlaylist;
                ArrayList<String> playlistNames = new ArrayList<>();
                for (int i = 0; i < playlists.size(); i++){
                    playlistNames.add(playlists.get(i).getPlaylist_name());
                }
                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, playlistNames);
//                adapter = new ChoosePlaylistAdapter(getContext(), playlists);
//                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NavController navController = NavHostFragment.findNavController(ChoosePlaylistFragment.this);
                        navController.getPreviousBackStackEntry().getSavedStateHandle().set("key", playlists.get(position).getPlaylist_id());
                        navController.popBackStack();
                    }
                });
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
