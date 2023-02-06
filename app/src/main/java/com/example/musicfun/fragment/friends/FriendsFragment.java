package com.example.musicfun.fragment.friends;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.musicfun.R;
import com.example.musicfun.databinding.FragmentFriendsBinding;
import com.example.musicfun.fragment.sharedplaylist.SharedPlaylistSongsFragment;
import com.example.musicfun.fragment.sharedplaylist.SharedPlaylistSongsFragmentDirections;
import com.example.musicfun.viewmodel.FriendsViewModel;
import com.google.android.material.navigation.NavigationBarView;

/**
 * This class hosts the Friends_friend_fragment view and SharedPlaylistFragment view,
 * depending on what is selected.
 */
public class FriendsFragment extends Fragment {

    private SharedPreferences sp;
    private FragmentFriendsBinding binding;

    FriendsViewModel friendsViewModel;
    private Toolbar toolbar;
    private SearchView searchView;
    private ImageView setting;
    private TextView invitees;
    NavController navController;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimpleFriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        boolean temp = isNetworkAvailable(getActivity().getApplication());
        if (!temp){
            System.out.println("network not connected!!");
            return;
        }

        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        navController = NavHostFragment.findNavController(getChildFragmentManager().findFragmentById(R.id.nav_host_friends));
        NavigationUI.setupWithNavController(binding.FriendsNav, navController);


        binding.FriendsNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                NavigationUI.onNavDestinationSelected(item, navController);
                return true;
            }
        });
        toolbar = getActivity().findViewById(R.id.toolbar);
        searchView = getActivity().findViewById(R.id.action_search);
        setting = getActivity().findViewById(R.id.setting);
        invitees = getActivity().findViewById(R.id.invitees);
//       special case: if language changed, the current visible fragment is refreshed in onCreate, hide NavView in this case.
        if(toolbar != null){
            destinationListener();
        }
    }

    private void destinationListener(){
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                if (navDestination.getId() == R.id.sharedPlaylistSongsFragment){
                    binding.FriendsNav.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    setting.setVisibility(View.GONE);
                    invitees.setVisibility(View.VISIBLE);
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setNavigationIcon(null);
                            binding.FriendsNav.setVisibility(View.VISIBLE);
                            navController.popBackStack();
                        }
                    });
                }
                else if (navDestination.getId() == R.id.sharedPlaylistParticipants3){
                    binding.FriendsNav.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    setting.setVisibility(View.GONE);
                    invitees.setVisibility(View.VISIBLE);
                    invitees.setText(getString(R.string.invitees));
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setNavigationIcon(null);
                            navController.popBackStack();
                        }
                    });
                }
                else if (navDestination.getId() == R.id.choosePlaylistFragment3){
                    searchView.setVisibility(View.GONE);
                    setting.setVisibility(View.GONE);
                    invitees.setVisibility(View.VISIBLE);
                    invitees.setText(getString(R.string.choose_playlist));
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setNavigationIcon(null);
                            navController.popBackStack();
                        }
                    });
                }
                else if (navDestination.getId() == R.id.list_of_friends_fragment){
                    binding.FriendsNav.setVisibility(View.GONE);
                    searchView.setVisibility(View.GONE);
                    setting.setVisibility(View.GONE);
                    invitees.setVisibility(View.VISIBLE);
                    invitees.setText(getString(R.string.invite_friends));
                    toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_purple);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            toolbar.setNavigationIcon(null);
                            navController.popBackStack();
                        }
                    });
                }
                else{
                    searchView.setVisibility(View.VISIBLE);
                    setting.setVisibility(View.VISIBLE);
                    invitees.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setSelectedPlaylistName(String selectedPlaylistName) {
        if(invitees != null){
            invitees.setText(selectedPlaylistName);
        }
    }

    public void setToolbar (){
        toolbar = getActivity().findViewById(R.id.toolbar);
        searchView = getActivity().findViewById(R.id.action_search);
        setting = getActivity().findViewById(R.id.setting);
        invitees = getActivity().findViewById(R.id.invitees);
        if(toolbar != null){
            destinationListener();
        }
    }

}