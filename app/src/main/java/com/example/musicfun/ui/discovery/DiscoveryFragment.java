package com.example.musicfun.ui.discovery;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.DiscoveryPart.DiscoveryChartsFragment;
import com.example.musicfun.DiscoveryPart.DiscoveryMayLikeFragment;
import com.example.musicfun.DiscoveryPart.DiscoveryMostHeardFragment;
import com.example.musicfun.DiscoveryPart.SimpleDiscoveryFragment;
import com.example.musicfun.R;
import com.example.musicfun.databinding.FragmentDiscoveryBinding;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.search.SearchResultAdapter;
import com.example.musicfun.search.Songs;

public class DiscoveryFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentDiscoveryBinding binding;
    private static final String TAG = "DiscoveryFragment";
    ListView listView;
    SearchResultAdapter adapter;
    SearchView editsearch;
    DiscoveryViewModel discoveryViewModel;
    public PassDataInterface mOnInputListner;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        binding = FragmentDiscoveryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        // locate the ListView in fragment_discovery.xml
        listView = binding.searchList;
        // pass results to ListViewAdapter Class
        adapter = new SearchResultAdapter(getActivity(), discoveryViewModel.songsArrayList);
        // binds the Adapter to the ListView
        listView.setAdapter(adapter);
        listView.setVisibility(View.INVISIBLE);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // hide soft keyboard if a user is scrolling the result list
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyboard();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                closeKeyboard();
                Songs s = (Songs) listView.getItemAtPosition(i);
                String input = s.getSongName();
                mOnInputListner.sendInput(input);
                listView.setVisibility(View.INVISIBLE);
                binding.DiscoveryNav.setVisibility(View.VISIBLE);
                binding.discoveryChildFragment.setVisibility((View.VISIBLE));
            }
        });
        // locate the SearchView in fragment_discovery.xml
        editsearch = binding.searchView;
        // search result list appears only if a user start searching
        editsearch.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                listView.setVisibility(View.VISIBLE);
                binding.DiscoveryNav.setVisibility(View.INVISIBLE);
                binding.discoveryChildFragment.setVisibility((View.INVISIBLE));
            }
        });
        editsearch.setOnQueryTextListener(this);

        // link to other song list fragments
        insertNestedFragment(new SimpleDiscoveryFragment());
        binding.DiscoveryNav.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case  R.id.new_releases:
                    insertNestedFragment(new SimpleDiscoveryFragment());
                    break;
                case R.id.most_heard:
                    insertNestedFragment(new DiscoveryMostHeardFragment());
                    break;
                case R.id.charts:
                    insertNestedFragment(new DiscoveryChartsFragment());
                    break;
                case R.id.may_like:
                    insertNestedFragment(new DiscoveryMayLikeFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        if(TextUtils.isEmpty(text)){
            listView.setVisibility(View.INVISIBLE);
        }
        else {
            listView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    private void closeKeyboard() {
        // this will give us the view which is currently focus in this layout
        View view = getActivity().getCurrentFocus();
        // if nothing is currently focus then this will protect the app from crash
        if (view != null) {
            // assign the system service to InputMethodManager
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void insertNestedFragment(Fragment childFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.discovery_childFragment, childFragment).commit();
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
}