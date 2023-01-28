package com.example.musicfun.fragment.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.LyricsActivity;
import com.example.musicfun.adapter.mymusic.SongListAdapter;
import com.example.musicfun.databinding.FragmentCurrentPlaylistBinding;
import com.example.musicfun.datatype.Songs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CurrentPlaylistFragment extends Fragment {

    private FragmentCurrentPlaylistBinding binding;
    private ListView lv_current;
    private ListView lv_next;
    private List<Songs> songInfo = new ArrayList<>();
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentCurrentPlaylistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lv_current = binding.lvCurrentTitle;
        lv_next = binding.lvNextTitles;
//       Toolbar handeling:
        toolbar = binding.toolbarLyrics;
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24));
        ((LyricsActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((LyricsActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(closeFragment);

        String json = CurrentPlaylistFragmentArgs.fromBundle(getArguments()).getPlaylist();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Songs>>(){}.getType();
        songInfo = gson.fromJson(json, type);
        if (songInfo.size() < 1){
            binding.tvNextTitles.setText(getString(R.string.no_songs));
            binding.tvCurrentTitle.setVisibility(View.INVISIBLE);
        }
        else if (songInfo.size() == 1){
            SongListAdapter adapter = new SongListAdapter(getContext(), songInfo);
            lv_current.setAdapter(adapter);
        }
        else{
            List<Songs> currentTitle = new ArrayList<>();
            currentTitle.add(songInfo.get(0));
            SongListAdapter adapter = new SongListAdapter(getContext(), currentTitle);
            lv_current.setAdapter(adapter);

            List<Songs> nextTitles = songInfo.subList(1, songInfo.size());
            SongListAdapter second_adapter = new SongListAdapter(getActivity(), nextTitles);
            lv_next.setAdapter(second_adapter);
        }
//      TODO: bind service here. If broadcast receive intent, update listview


    }

    private View.OnClickListener closeFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavController navController = NavHostFragment.findNavController(CurrentPlaylistFragment.this);
            navController.popBackStack();
        }
    };

}
