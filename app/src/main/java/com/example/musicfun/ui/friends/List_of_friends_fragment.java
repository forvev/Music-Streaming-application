package com.example.musicfun.ui.friends;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.musicfun.R;
import com.example.musicfun.adapter.friends.FriendsListAdapter;
import com.example.musicfun.adapter.friends.FriendsSharedListAdapter;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;

import java.util.ArrayList;

public class List_of_friends_fragment extends Fragment {

    FriendsViewModel friendsViewModel;
    ListView listView;
    SharedPreferences sp;
    FriendsSharedListAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public List_of_friends_fragment() {
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
    public static List_of_friends_fragment newInstance(String param1, String param2) {
        List_of_friends_fragment fragment = new List_of_friends_fragment();
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

        return inflater.inflate(R.layout.fragment_friends_shared_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        friendsViewModel.init("user/allFriends?auth_token=" + sp.getString("token", ""));
        listView = (ListView) view.findViewById(R.id.list_v_shared_playlist);
        friendsViewModel.getUserNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                adapter = new FriendsSharedListAdapter(getActivity(), users, friendFragmentInterface);
                listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged(); Not working like that!

            }
        });


    }


}
