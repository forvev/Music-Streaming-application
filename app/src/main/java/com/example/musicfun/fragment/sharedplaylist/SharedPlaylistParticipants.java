package com.example.musicfun.fragment.sharedplaylist;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.musicfun.adapter.friends.ParticipantsSharedListAdapter;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;
import com.example.musicfun.viewmodel.FriendsViewModel;

import java.util.ArrayList;

/**implementation of the list of participants
 * In other words this fragment will display a list of friends with which we share our playlist
 *
 */
public class SharedPlaylistParticipants extends Fragment {

    FriendsViewModel friendsViewModel;
    SharedPreferences sp;
    ListView listView;
    ParticipantsSharedListAdapter adapter;
    String passed_playlist_id;

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
        public void deleteFriend(int position, String name) {
        }

        @Override
        public void deleteFriend(int position, String user_id, String playlist_id) {
            playlist_id = passed_playlist_id;
            friendsViewModel.delete_user_from_shared_playlist("playlist/deleteUserFromSharedPlaylist?auth_token=" + sp.getString("token", ""), user_id, playlist_id, position);
            Toast.makeText(getContext(),"Deleted",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void addFriend(String name, int i) {

        }

        @Override
        public void getProfile(int i) {

        }

        @Override
        public void startChat(String name) {

        }

        @Override
        public void add_friends(ArrayList<String> arrayList) {

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

        passed_playlist_id = SharedPlaylistParticipantsArgs.fromBundle(getArguments()).getSelectedSharedId3();
        Log.i("passed",passed_playlist_id);
        friendsViewModel.fetch_shared_friends("playlist/getUsersFromSharedPlaylist?auth_token=" + sp.getString("token", ""),passed_playlist_id);

        //user/allFriendsForSharedPlaylist
        //playlist/getUsersFromSharedPlaylist
        listView = (ListView) view.findViewById(R.id.list_v_shared_playlist_participants);
        friendsViewModel.getUserNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                Log.i("users:",String.valueOf(users));
                adapter = new ParticipantsSharedListAdapter(getActivity(), users, friendFragmentInterface);
                listView.setAdapter(adapter);
            }
        });

    }

}
