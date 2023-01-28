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
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.activity.LyricsActivity;
import com.example.musicfun.adapter.SharedPlaylist.ActiveListenerAdapter;
import com.example.musicfun.databinding.FragmentActiveListenersBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ActiveListenerFragment extends Fragment {

    private FragmentActiveListenersBinding binding;
    private ListView lv_acitveListeners;
    private Toolbar toolbar;
    private ImageView iv_activeListeners;
//    private MainActivityViewModel viewModel;

    ActiveListenerAdapter adapter;
    ArrayList<String> activeListeners = new ArrayList<>();
    MutableLiveData<ArrayList<String>> activeListenersLive = new MutableLiveData<>();
    ListView lv_listeners;


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
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_close_24));
        ((LyricsActivity)getActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((LyricsActivity)getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(closeFragment);

        lv_acitveListeners =  binding.lvActiveListeners;
        //lv_listeners = (ListView) view.findViewById(R.id.lv_active_listeners);
//        TODO: set list adapter and fetch active users
        String json = ActiveListenerFragmentArgs.fromBundle(getArguments()).getUsernames();
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> usernames = gson.fromJson(json, type);
        if(!usernames.isEmpty()) {
            adapter = new ActiveListenerAdapter(getContext(),usernames);
            lv_acitveListeners.setAdapter(adapter);
        }
        //Maybe later
        /*
        Fragment fragment = getParentFragmentManager().findFragmentById(Navigation.findNavController(view).getPreviousBackStackEntry().getDestination().getId());
        ((LyricsFragment) fragment).getUsernamesLive().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {
                if(!strings.isEmpty()) {
                    adapter = new ActiveListenerAdapter(getContext(), strings);
                    lv_acitveListeners.setAdapter(adapter);
                }
            }
        });
         */

    }

    private View.OnClickListener closeFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            NavController navController = NavHostFragment.findNavController(ActiveListenerFragment.this);
            navController.popBackStack();
        }
    };
}
