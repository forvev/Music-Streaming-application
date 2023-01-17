package com.example.musicfun.fragment.sharedplaylist;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.musicfun.R;
import com.example.musicfun.adapter.friends.FriendsSharedListAdapter;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;
import com.example.musicfun.ui.friends.FriendsViewModel;
import com.example.musicfun.ui.friends.List_of_friends_fragment;
import com.example.musicfun.ui.friends.List_of_friends_fragmentArgs;
import com.example.musicfun.ui.friends.List_of_friends_fragmentDirections;

import java.util.ArrayList;

public class SharedPlaylistParticipants extends Fragment {

    FriendsViewModel friendsViewModel;
    SharedPreferences sp;
    ListView listView;
    FriendsSharedListAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SharedPlaylistParticipants() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Friends_shared_playlist_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SharedPlaylistParticipants newInstance(String param1, String param2) {
        SharedPlaylistParticipants fragment = new SharedPlaylistParticipants();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

        }

    };

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

        return inflater.inflate(R.layout.fragment_shared_playlist_participants, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String passed_playlist_id = SharedPlaylistParticipantsArgs.fromBundle(getArguments()).getSelectedSharedId3();
        Log.i("passed",passed_playlist_id);
        friendsViewModel.fetch_shared_friends("playlist/getUsersFromSharedPlaylist?auth_token=" + sp.getString("token", ""),passed_playlist_id);
        //friendsViewModel.fetch_shared_friends("user/allFriendsForSharedPlaylist?auth_token=" + sp.getString("token", ""),passed_playlist_id);

        //user/allFriendsForSharedPlaylist
        //playlist/getUsersFromSharedPlaylist
        listView = (ListView) view.findViewById(R.id.list_v_shared_playlist);
        friendsViewModel.getUserNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                Log.i("users:",String.valueOf(users));
                adapter = new FriendsSharedListAdapter(getActivity(), users, friendFragmentInterface);
                listView.setAdapter(adapter);
            }
        });

//        ImageView imageView_delete = (ImageView) view.findViewById(R.id.shared_playlist_friends_custom_delete);
//
//        imageView_delete.setOnClickListener(click ->{
//            Log.i("hello","hehe");
//        });
    }

}
