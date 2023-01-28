package com.example.musicfun.adapter.friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.musicfun.R;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;
import com.example.musicfun.ui.friends.Friends_DBAccess;

import java.util.ArrayList;
import java.util.List;

public class FriendsSharedListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<User> userList = null;
    private ArrayList<User> arrayList;
    Friends_DBAccess dbAccess;
    SharedPreferences sp;
    FriendFragmentInterface fi;
    private ArrayList<String> selected_users_list;


    public FriendsSharedListAdapter(Context context, List<User> userList, FriendFragmentInterface fi){
        mContext = context;
        this.userList = userList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(userList);
        dbAccess = new Friends_DBAccess(mContext);
        this.fi = fi;
    }


    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int i) {
        return userList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class SharedPlaylistParticipantsHolder {
        private TextView user_name;
        private RadioButton btn_selected;
        private Button button_arrow;


    }
    //TODO: move this class to FriendslistAdapter. The problem is with the view below,
    //because I need to invoke different one.
    //Adapters call the getView() method which returns a view for each item within the adapter view.
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        SharedPlaylistParticipantsHolder holder;
        if (view == null){
            holder = new SharedPlaylistParticipantsHolder();
            view = inflater.inflate(R.layout.shared_playlist_friends_custom_v, null);
            holder.user_name = (TextView) view.findViewById(R.id.friends_shared_playlist_custom_view_username);
            holder.btn_selected = (RadioButton) view.findViewById(R.id.btn_select_shared_friends);
            holder.button_arrow = (Button) view.findViewById(R.id.buttonSharedFriends);

            holder.user_name.setText(userList.get(position).getUserName());
            //view.setTag(holder);
        }else{
            holder = (SharedPlaylistParticipantsHolder) view.getTag();
        }

        selected_users_list = new ArrayList<>();
        holder.btn_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected_users_list.contains(userList.get(position).getUser_id())){
                    selected_users_list.remove(userList.get(position).getUser_id());
                    holder.btn_selected.setChecked(false);
                    //remove selected user to the list
                    //selected_users_list.remove(users_list.get(position));
                    //if there are no selected items we can hide the button
                    //if (selected_users_list.isEmpty())  holder.button_arrow.setVisibility(View.INVISIBLE);
                    fi.add_friends(selected_users_list);
                }
                else{
                    holder.btn_selected.setChecked(true);
                    //add selected user to the list
                    selected_users_list.add(userList.get(position).getUser_id());
                    //Log.i("User_name",String.valueOf(users_list.get(position).getUser_id()));
                    //selectedItems.add(position);
                    //holder.button_arrow.setVisibility(View.VISIBLE);
                    fi.add_friends(selected_users_list);
                }
            }
        });
        return view;
    }
}
