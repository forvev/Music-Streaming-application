package com.example.musicfun.DiscoveryPart;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.R;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.datatype.Songs;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryChartsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryChartsFragment extends Fragment {

    ListView listView;
    public PassDataInterface mOnInputListner;
    DiscoveryFragmentAdapter adapter;
    DiscoveryChartsViewModel discoveryChartsViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoveryChartsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoveryChartsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryChartsFragment newInstance(String param1, String param2) {
        DiscoveryChartsFragment fragment = new DiscoveryChartsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        discoveryChartsViewModel = new ViewModelProvider(this).get(DiscoveryChartsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_discovery_charts, container, false);
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

        discoveryChartsViewModel.init();

        listView = (ListView)view.findViewById(R.id.lvdiscovery);

        discoveryChartsViewModel.getSongNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<Songs>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Songs> newName) {
                adapter = new DiscoveryFragmentAdapter(getActivity(), newName);
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