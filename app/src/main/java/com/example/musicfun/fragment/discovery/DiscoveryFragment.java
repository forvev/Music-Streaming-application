package com.example.musicfun.fragment.discovery;

import static android.content.Context.MODE_PRIVATE;

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
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.R;
import com.example.musicfun.activity.RegisterActivity;
import com.example.musicfun.activity.SettingActivity;
import com.example.musicfun.databinding.FragmentDiscoveryBinding;
import com.example.musicfun.interfaces.PassDataInterface;
import com.example.musicfun.adapter.search.SearchResultAdapter;
import com.example.musicfun.datatype.Songs;
import com.example.musicfun.viewmodel.discovery.DiscoveryViewModel;

import java.util.ArrayList;

public class DiscoveryFragment extends Fragment {

    private FragmentDiscoveryBinding binding;
    private static final String TAG = "DiscoveryFragment";
    ListView listView;
    SearchResultAdapter adapter;
    SearchView searchView;
    DiscoveryViewModel discoveryViewModel;
    public PassDataInterface mOnInputListner;
    private SharedPreferences sp;

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

        // check internet connection
        boolean temp = isNetworkAvailable(getActivity().getApplication());
        if (!temp){
            System.out.println("network not connected!!");
            return;
        }

        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        int state = sp.getInt("logged", 999);
        binding.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSetting = new Intent(getActivity(), SettingActivity.class);
//                System.out.println("state current = " + state);
                if(state ==0){
                    Intent gotoLogin = new Intent(getActivity(), RegisterActivity.class);
                    sp.edit().putInt("logged", -1).apply();
                    Toast.makeText(getContext(), R.string.login_required, Toast.LENGTH_SHORT).show();
//                    System.out.println("state after = " + sp.getInt("logged", 999));
                    getActivity().startActivity(gotoLogin);
                }
                else{
                    getActivity().startActivity(gotoSetting);
                }
            }
        });

        // locate the SearchView in fragment_discovery.xml
        searchView = binding.searchView;
        // search result list appears only if a user start searching
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(listView != null){
                    listView.setVisibility(View.VISIBLE);
                }
                binding.setting.setVisibility(View.GONE);
                binding.cancel.setVisibility(View.VISIBLE);
                // cancel the search
                binding.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeKeyboard(view);
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        listView.setVisibility(View.INVISIBLE);
                        binding.DiscoveryNav.setVisibility(View.VISIBLE);
                        binding.discoveryChildFragment.setVisibility((View.VISIBLE));
                        binding.setting.setVisibility(View.VISIBLE);
                        binding.cancel.setVisibility(View.GONE);
                    }
                });
                discoveryViewModel.init("get/allSongs");
                binding.DiscoveryNav.setVisibility(View.INVISIBLE);
                binding.discoveryChildFragment.setVisibility((View.INVISIBLE));

                // locate the ListView in fragment_discovery.xml
                listView = binding.searchList;
                // pass results to ListViewAdapter Class
                discoveryViewModel.getSongNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<Songs>>() {
                    @Override
                    public void onChanged(@Nullable final ArrayList<Songs> newName) {
                        adapter = new SearchResultAdapter(getActivity(), newName);
                        // binds the Adapter to the ListView
                        listView.setAdapter(adapter);
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }
                            @Override
                            public boolean onQueryTextChange(String newText) {
                                String text = newText;
                                discoveryViewModel.filter(text);
                                return true;
                            }
                        });
                    }
                });
                listView.setOnTouchListener(new View.OnTouchListener() {
                    // hide soft keyboard if a user is scrolling the result list
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        closeKeyboard(v);
                        return false;
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        closeKeyboard(view);
                        Songs s = (Songs) listView.getItemAtPosition(i);
                        int id = s.getSongId();
                        mOnInputListner.sendInput(Integer.toString(id));
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        listView.setVisibility(View.INVISIBLE);
                        binding.DiscoveryNav.setVisibility(View.VISIBLE);
                        binding.discoveryChildFragment.setVisibility((View.VISIBLE));
                        binding.setting.setVisibility(View.VISIBLE);
                        binding.cancel.setVisibility(View.GONE);
                    }
                });
            }
        });

        // link to other song list fragments
        insertNestedFragment(new NewReleaseFragment());
        binding.DiscoveryNav.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case  R.id.new_releases:
                    insertNestedFragment(new NewReleaseFragment());
                    break;
                case R.id.most_heard:
                    insertNestedFragment(new MostHeardFragment());
                    break;
                case R.id.charts:
                    insertNestedFragment(new ChartsFragment());
                    break;
                case R.id.may_like:
                    insertNestedFragment(new MayLikeFragment());
                    break;
            }
            return true;
        });
    }

    private Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    private void closeKeyboard(View view) {
        // this will give us the view which is currently focus in this layout
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