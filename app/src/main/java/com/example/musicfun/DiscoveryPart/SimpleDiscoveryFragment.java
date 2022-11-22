package com.example.musicfun.DiscoveryPart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicfun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SimpleDiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

//this is Releases class
public class SimpleDiscoveryFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvDiscovery;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SimpleDiscoveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimpleDiscoveryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SimpleDiscoveryFragment newInstance(String param1, String param2) {
        SimpleDiscoveryFragment fragment = new SimpleDiscoveryFragment();
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
        View view = inflater.inflate(R.layout.fragment_simple_discovery, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] songs = {"Sweet Child O' Mine", "Kickstart My Heart","The Final Countdown",
                "Eye of the Tiger", "Crazy Train", "Hells Bells", "Poison", "Back in Black",
                "Paradise City", "Summer of '69", "Ace of Spades", "Welcome to the Jungle",
                "Personal Jesus", "Panama", "Africa", "Don't stop believin'", "The Boys of Summer",
                "Black Betty", "T.N.T", "Paranoid", "Sweet Home Alabama", "Highway to Hell"};

        ListView listView = (ListView)view.findViewById(R.id.lvdiscovery);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_expandable_list_item_1,songs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String song = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getActivity(), "Clicked: "+ song, Toast.LENGTH_SHORT).show();
    }
}