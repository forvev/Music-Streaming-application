package com.example.musicfun.fragment.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.LyricsActivity;
import com.example.musicfun.databinding.FragmentActiveListenersBinding;
import com.example.musicfun.datatype.Songs;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class ActiveListenerFragment extends Fragment {

    private FragmentActiveListenersBinding binding;
    private ListView lv_acitveListeners;
    private TextView noActive;
    private Toolbar toolbar;
    private ImageView iv_activeListeners;
//    private MainActivityViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentActiveListenersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//       Toolbar handeling:
        toolbar = binding.toolbarLyrics;
        toolbar.setTitle("Active users");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24));
        ((LyricsActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((LyricsActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(closeFragment);

        lv_acitveListeners =  binding.lvActiveListeners;
        noActive = binding.noActiveListeners;
//        TODO: set list adapter and fetch active users
        String json = ActiveListenerFragmentArgs.fromBundle(getArguments()).getUsernames();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> usernames = gson.fromJson(json, type);
        if(!usernames.isEmpty()) {
            noActive.setVisibility(View.GONE);
        }

    }

    private View.OnClickListener closeFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavController navController = NavHostFragment.findNavController(ActiveListenerFragment.this);
            navController.popBackStack();
        }
    };
}
