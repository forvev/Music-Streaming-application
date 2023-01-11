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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.musicfun.R;
import com.example.musicfun.activity.MessageListActivity;
import com.example.musicfun.adapter.FriendsListAdapter;
import com.example.musicfun.databinding.FragmentFriendsBinding;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Friends_friend_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Friends_friend_Fragment extends Fragment {

    ListView listView;
    FriendsViewModel friendsViewModel;
    SharedPreferences sp;
    FriendsListAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_PARAM_NAME = "chatPartnerName";

    // TODO: Rename and change types of parameters
    private FriendFragmentInterface friendFragmentInterface = new FriendFragmentInterface() {
        @Override
        public void deleteFriend(int i) {
            Toast.makeText(getContext(),"Deleted ",Toast.LENGTH_SHORT).show();

            friendsViewModel.sendMsgWithBodyDelete("user/deleteFriend?auth_token=" + sp.getString("token", ""),i);
        }

        @Override
        public void addFriend(String name) {

        }

        @Override
        public void getProfile(int i) {

        }

        @Override
        public void startChat(String name) {
            //Log.d("disTest", name);
            Intent goChat = new Intent(getActivity(), MessageListActivity.class);
            goChat.putExtra(ARG_PARAM_NAME, name);
            getActivity().startActivity(goChat);

        }
    };
    private String mParam1;
    private String mParam2;

    public Friends_friend_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friends_friend_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Friends_friend_Fragment newInstance(String param1, String param2) {
        Friends_friend_Fragment fragment = new Friends_friend_Fragment();
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
        friendsViewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_friends_friend_, container, false);
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


        friendsViewModel.init("user/allFriends?auth_token=" + sp.getString("token", ""));
        listView = (ListView) view.findViewById(R.id.lvfriends);
        friendsViewModel.getUserNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                adapter = new FriendsListAdapter(getActivity(), users, friendFragmentInterface);
                listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged(); Not working like that!
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