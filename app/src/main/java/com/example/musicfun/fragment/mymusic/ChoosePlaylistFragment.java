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
import com.example.musicfun.adapter.search.SearchPlaylistAdapter;
import com.example.musicfun.databinding.FragmentChoosePlaylistBinding;
import com.example.musicfun.datatype.Playlist;
import com.example.musicfun.viewmodel.mymusic.PlaylistViewModel;

import java.util.ArrayList;

public class ChoosePlaylistFragment extends Fragment {

    private FragmentChoosePlaylistBinding binding;
    private PlaylistViewModel viewModel;
    private ListView listView;
    private TextView cancel;
    private LinearLayout create_new_playlist;
    private SearchView searchView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> searchAdapter;

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
        viewModel.getAllPlaylists();
        viewModel.getM_playlist().observe(getViewLifecycleOwner(), new Observer<ArrayList<Playlist>>(){
            @Override
            public void onChanged(ArrayList<Playlist> playlists) {
                listView = binding.chooseOnePlaylist;
                ArrayList<String> playlistNames = new ArrayList<>();
                for (int i = 0; i < playlists.size(); i++){
                    playlistNames.add(playlists.get(i).getPlaylist_name());
                }
                arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, playlistNames);
                listView.setAdapter(arrayAdapter);
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
        // cancel this fragment
        cancel = binding.cancel;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = NavHostFragment.findNavController(ChoosePlaylistFragment.this);
                navController.getPreviousBackStackEntry().getSavedStateHandle().set("key", null);
                navController.popBackStack();
            }
        });


        // Search the wanted playlist. Here we will reuse the listview which is already shown on the screen.
//        searchView = binding.searchView;
//        searchView.setOnQueryTextFocusChangeListener(searchClickListner);
//        searchView.setQueryHint("Find playlist");
    }

//    private View.OnFocusChangeListener searchClickListner = new View.OnFocusChangeListener() {
//        @Override
//        public void onFocusChange(View view, boolean b) {
//            create_new_playlist.setVisibility(View.GONE);
//            // pass results to ListViewAdapter Class
//            viewModel.getM_searchResult().observe(getViewLifecycleOwner(), new Observer<ArrayList<Playlist>>() {
//                @Override
//                public void onChanged(@Nullable final ArrayList<Playlist> newName) {
//                    ArrayList<String> playlistNames = new ArrayList<>();
//                    for (int i = 0; i < newName.size(); i++){
//                        playlistNames.add(newName.get(i).getPlaylist_name());
//                    }
//                    searchAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_single_choice, playlistNames);
//                    // binds the Adapter to the ListView
//                    listView.setAdapter(searchAdapter);
//                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                        @Override
//                        public boolean onQueryTextSubmit(String query) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onQueryTextChange(String newText) {
//                            String text = newText;
//                            viewModel.searchPlaylistByName(text);
//                            return true;
//                        }
//                    });
//                }
//            });
//            listView.setOnTouchListener(new View.OnTouchListener() {
//                // hide soft keyboard if a user is scrolling the result list
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    closeKeyboard(v);
//                    return false;
//                }
//            });
//            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    closeKeyboard(view);
//                    Playlist p = listView.getItemAtPosition(i);
//                    String id = p.getPlaylist_id();
//                    searchView.setQuery("", false);
//                    searchView.clearFocus();
//                    searchResult.setVisibility(View.INVISIBLE);
//                    listView.setVisibility(View.VISIBLE);
//                    add_playlist.setVisibility((View.VISIBLE));
//                    binding.setting.setVisibility(View.VISIBLE);
//                    binding.cancel.setVisibility(View.GONE);
//                    System.out.println("searched playlist id = " + id);
//                    NavDirections action = MyMusicFragmentDirections.actionMyMusicToMyPlaylistFragment(id);
//                    Navigation.findNavController(getView()).navigate(action);
//                }
//            });
//        }
//    };

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
