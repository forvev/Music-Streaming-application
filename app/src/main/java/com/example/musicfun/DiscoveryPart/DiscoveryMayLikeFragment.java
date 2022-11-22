package com.example.musicfun.DiscoveryPart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicfun.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoveryMayLikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoveryMayLikeFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView lvDiscovery;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoveryMayLikeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoveryMayLikeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoveryMayLikeFragment newInstance(String param1, String param2) {
        DiscoveryMayLikeFragment fragment = new DiscoveryMayLikeFragment();
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
        View view = inflater.inflate(R.layout.fragment_discovery_may_like, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] songs = {"Ein Schiff wird kommen", "Seemann(Deine Heimat ist das Meer)","Veronica, der Lenz ist da",
                "Es liegt was in der Luft", "Bella Bimba", "Das alte Försterhaus", "Heideröslein", "Wir, wir, wir haben ein Klavier",
                "Jambalaya", "Der lachende Vagabund", "Mary-Rose", "Wenn es Nacht wird in Montana",
                "Heimatlos", "Heimweh", "Der Legionär", "Tschau Tschau Bambina", "Quando",
                "Wo meine Sonne scheint", "Heißer Sand", "Die Gitarre und das Meer", "Anneliese", "Die Fischerin vom Bodensee"};

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