package com.example.musicfun.ui.friends;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.musicfun.R;
import com.example.musicfun.activity.RegisterActivity;
import com.example.musicfun.activity.SettingActivity;
import com.example.musicfun.databinding.FragmentFriendsBinding;

public class FriendsFragment extends Fragment {

    private SharedPreferences sp;
    private FragmentFriendsBinding binding;

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

        insertNestedFragment(new Friends_friend_Fragment());
        binding.FriendsNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.friends_friend:
                    insertNestedFragment(new Friends_friend_Fragment());
                    break;
                case R.id.friends_sharedPlaylist:
                    insertNestedFragment(new Friends_shared_playlist_Fragment());
                    break;
            }
            return true;
        });

        sp = getContext().getSharedPreferences("login", MODE_PRIVATE);
        int state = sp.getInt("logged", 999);
        binding.friendsSetting.setOnClickListener(new View.OnClickListener() {
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
    }

    private void insertNestedFragment(Fragment childFragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.friends_childFragment, childFragment).commit();
    }

    private Boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }
}