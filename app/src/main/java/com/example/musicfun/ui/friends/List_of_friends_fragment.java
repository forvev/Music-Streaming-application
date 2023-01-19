package com.example.musicfun.ui.friends;

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
import com.example.musicfun.adapter.friends.FriendsListAdapter;
import com.example.musicfun.adapter.friends.FriendsSharedListAdapter;
import com.example.musicfun.datatype.User;
import com.example.musicfun.fragment.sharedplaylist.SharedPlaylistFragmentDirections;
import com.example.musicfun.interfaces.FriendFragmentInterface;


import java.util.ArrayList;
import java.util.HashSet;

public class List_of_friends_fragment extends Fragment {

    FriendsViewModel friendsViewModel;
    ListView listView;
    SharedPreferences sp;
    FriendsSharedListAdapter adapter;
    //to check if the item in the listView was selected
    ArrayList<User> users_list;//, selected_users_list;
    String selected_shared_id_2;

    //private HashSet<String> selectedItems = new HashSet<>();
    private ArrayList<String> selected_users_list;
    private long mLastClickTime = 0;

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
        public void deleteFriend(int position, String user_id, String playlist_id) {

        }

        //@Override
        //public void deleteFriend(int position, String user_id, String playlist_id) {

        //}

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

        //Find selected playlist id for the request to db
        selected_shared_id_2 = List_of_friends_fragmentArgs.fromBundle(getArguments()).getSelectedSharedId2();
        Log.i("play_id", selected_shared_id_2);
        //friendsViewModel.init("user/allFriends?auth_token=" + sp.getString("token", ""));
        friendsViewModel.fetch_shared_friends("user/allFriendsForSharedPlaylist?auth_token=" + sp.getString("token", ""),selected_shared_id_2);
        listView = (ListView) view.findViewById(R.id.list_v_shared_playlist);
        friendsViewModel.getUserNames().observe(getViewLifecycleOwner(), new Observer<ArrayList<User>>() {
            @Override
            public void onChanged(ArrayList<User> users) {
                adapter = new FriendsSharedListAdapter(getActivity(), users, friendFragmentInterface);
                users_list = new ArrayList<>();
                users_list = users;
                listView.setAdapter(adapter);
                //adapter.notifyDataSetChanged(); Not working like that!

            }
        });

        Button my_button = (Button)view.findViewById(R.id.buttonSharedFriends);
        my_button.setVisibility(View.INVISIBLE);
        selected_users_list = new ArrayList<>();

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(@NonNull View view, MotionEvent motionEvent) {

                // mis-clicking prevention, using threshold of 500 ms
                if (SystemClock.elapsedRealtime() - mLastClickTime < 100){
                    return false;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                int position = listView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
                if (position == -1){
                    return false;
                }else{
                    Log.i("info of users",String.valueOf(position));
                    View item = listView.getChildAt(position);

                    String user_id = users_list.get(position).getUser_id();


                    if (selected_users_list.contains(user_id)){
                        item.setBackgroundColor(Color.WHITE);
                        selected_users_list.remove(user_id);
                        //remove selected user to the list
                        //selected_users_list.remove(users_list.get(position));
                        //if there are no selected items we can hide the button
                        if (selected_users_list.isEmpty())  my_button.setVisibility(View.INVISIBLE);
                    }
                    else{
                        item.setBackgroundColor(Color.rgb(177, 151, 229));
                        //add selected user to the list
                        selected_users_list.add(user_id);
                        //Log.i("User_name",String.valueOf(users_list.get(position).getUser_id()));
                        //selectedItems.add(position);
                        my_button.setVisibility(View.VISIBLE);
                    }
                    Log.i("actual users",String.valueOf(selected_users_list));
                    return false;
                }
            }
        });


        my_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("selected_users",String.valueOf(selected_users_list));

                for(int i=0; i<selected_users_list.size(); i++){
                    Log.i("user:",selected_users_list.get(i));
                    friendsViewModel.add_user_to_shared_playlist("playlist/addUserToSharedPlaylist?auth_token=" + sp.getString("token", ""),selected_users_list.get(i), selected_shared_id_2);
                }

                NavDirections action = List_of_friends_fragmentDirections.actionListOfFriendsFragmentToFriendsSharedPlaylist4();
                //NavDirections action = List_of_friends_fragmentDirections.actionListOfFriendsFragmentToSharedPlaylistParticipants2();
                Navigation.findNavController(getView()).navigate(action);
            }
        });

    }


}
