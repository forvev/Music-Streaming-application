package com.example.musicfun.fragment.discovery;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.musicfun.R;
import com.example.musicfun.adapter.discovery.SongListAdapter;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.interfaces.SonglistMenuClick;
import com.example.musicfun.viewmodel.discovery.DiscoveryViewModel;

import java.util.ArrayList;

/**
 * Displays a list of songs that will be based on friends' history
 */
public class MostHeardFragment extends Fragment {
    SharedPreferences sp;
    ListView listView;
    public PassDataInterface mOnInputListner;
    SongListAdapter adapter;
    DiscoveryViewModel discoveryViewModel;

    private SonglistMenuClick songlistMenuClick = new SonglistMenuClick() {
        @Override
        public void removeFromPlaylist(int position) {

        }

        @Override
        public void addToPlaylist(String songId) {
            NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
            Fragment parent = (Fragment) navHostFragment.getParentFragment();
            ((DiscoveryFragment)parent).changeFragement(songId);
        }

        @Override
        public void addToDefault(String position) {
            discoveryViewModel.getDefaultPlaylist(position);
        }
    };
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MostHeardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoveryMostHeardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MostHeardFragment newInstance(String param1, String param2) {
        MostHeardFragment fragment = new MostHeardFragment();
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
        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        discoveryViewModel = new ViewModelProvider(this).get(DiscoveryViewModel.class);
        View view = inflater.inflate(R.layout.fragment_discovery_most_heard, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean temp = isNetworkAvailable(getActivity().getApplication());
        if(!temp){
            System.out.println("Network not connected!!!");
            return;
        }

        discoveryViewModel.init("get/popularSongsAmongFriends?auth_token=" + sp.getString("token", ""));

        listView = (ListView)view.findViewById(R.id.lvdiscovery);

        discoveryViewModel.getSongNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<Songs>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Songs> newName) {
                adapter = new SongListAdapter(getActivity(), newName, songlistMenuClick);
                listView.setAdapter(adapter);
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
}