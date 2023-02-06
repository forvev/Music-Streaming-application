package com.example.musicfun.fragment.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.adapter.login.GridViewAdapter;
import com.example.musicfun.databinding.FragmentGenreBinding;
import com.example.musicfun.datatype.Genre;
import com.example.musicfun.fragment.mymusic.ChoosePlaylistFragment;
import com.example.musicfun.viewmodel.login.GenreViewModel;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * This fragment allows you to choose the genres from the provided list
 */
public class GenreFragment extends Fragment {

    private GridView gridView;
    private FragmentGenreBinding binding;
    private GenreViewModel genreViewModel;
    private Button button;
    GridViewAdapter adapter;
    Boolean fromSetting = false;
    // https://icons8.com/icons/set/music-genre

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        genreViewModel = new ViewModelProvider(this).get(GenreViewModel.class);
        binding = FragmentGenreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = binding.genre;
        button = binding.submitGenre;
        fromSetting = GenreFragmentArgs.fromBundle(getArguments()).getFromSetting();
        if(fromSetting){
            button.setText(R.string.save);
            button.setTextSize(24);
        }
        try {
            genreViewModel.init();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        genreViewModel.getGenreLiveList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Genre>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Genre> genres) {
                adapter = new GridViewAdapter(genres, getContext());
                // binds the Adapter to the ListView
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(!genres.get(i).getSelected()) {
                            view.setBackground(getResources().getDrawable(R.drawable.rounded_corner_view, getContext().getTheme()));
                        }
                        else{
                            view.setBackground(null);
                        }
                        genres.get(i).setSelected();
                    }
                });
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sends out the selected genre ids
                try {
                    genreViewModel.submit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(fromSetting){
                    NavController navController = NavHostFragment.findNavController(GenreFragment.this);
                    navController.popBackStack();
                }
                else{
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);
                }
            }
        });

    }
}
