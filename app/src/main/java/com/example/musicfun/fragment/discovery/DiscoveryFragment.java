package com.example.musicfun.fragment.discovery;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.R;
import com.example.musicfun.activity.MainActivity;
import com.example.musicfun.activity.RegisterActivity;
import com.example.musicfun.activity.SettingActivity;
import com.example.musicfun.databinding.FragmentDiscoveryBinding;
import com.example.musicfun.fragment.mymusic.MyPlaylistFragment;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.adapter.search.SearchResultAdapter;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.viewmodel.discovery.DiscoveryViewModel;
import com.example.musicfun.viewmodel.mymusic.SonglistViewModel;
import com.google.android.exoplayer2.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This fragment shows us sections to choose like new releases or friends' taste
 * Moreover the navigation graph is implemented here
 */
public class DiscoveryFragment extends Fragment {

    private FragmentDiscoveryBinding binding;
    private static final String TAG = "DiscoveryFragment";
    DiscoveryViewModel discoveryViewModel;
    private SonglistViewModel songlistViewModel;
    public PassDataInterface mOnInputListner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String song_id;
    private String added_PlaylistId = "";
    private String added_SongId = "";
    private boolean hasAdded = false;
    private boolean forceAdd;

    public DiscoveryFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Discovery_Decision_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryFragment newInstance(String param1, String param2) {
        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        discoveryViewModel = new ViewModelProvider(this).get(DiscoveryViewModel.class);
        songlistViewModel = new ViewModelProvider(this).get(SonglistViewModel.class);
        binding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // check internet connection
        boolean temp = isNetworkAvailable(getActivity().getApplication());
        if (!temp){
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sp = getActivity().getSharedPreferences("login",MODE_PRIVATE);
        NavController controller = NavHostFragment.findNavController(getChildFragmentManager().findFragmentById(R.id.nav_host_discovery));
        if(sp.getInt("logged",0) == 0){
            binding.DiscoveryNav.getMenu().removeItem(R.id.most_heard);
            binding.DiscoveryNav.getMenu().removeItem(R.id.may_like);
        }
        NavigationUI.setupWithNavController(binding.DiscoveryNav, controller);

        NavController navController = NavHostFragment.findNavController(DiscoveryFragment.this);
        MutableLiveData<String> liveData = navController.getCurrentBackStackEntry().getSavedStateHandle().getLiveData("key");
        liveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String playlist_position) {
                if(playlist_position != null && song_id != null && (forceAdd || (!playlist_position.equals(added_PlaylistId) || !song_id.equals(added_SongId)))){
                    forceAdd = false;
                    added_PlaylistId = playlist_position;
                    added_SongId = song_id;
                    discoveryViewModel.addSongToPlaylist(playlist_position, song_id);
                }
            }
        });
    }

    private Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    // rewrite onAttach to send item clicked in the list back to main activity
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            mOnInputListner = (PassDataInterface) getActivity();
        }catch(ClassCastException e){
            Log.e(TAG, "onAttach: ClassCast WRONG " + e.getMessage());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View getContainer(){
        return getView().findViewById(R.id.DiscoveryNav);
    }

    public void changeFragement(String song_id){
        NavDirections action = DiscoveryFragmentDirections.actionDiscoveryToChooseOnePlaylist();
        Navigation.findNavController(getView()).navigate(action);
        this.song_id = song_id;
        this.forceAdd = true;
    }

}