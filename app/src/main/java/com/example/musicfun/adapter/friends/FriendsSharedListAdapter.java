package com.example.musicfun.adapter.friends;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
        ListView listView;
        ImageView imageView_delete;
    }
    //TODO: move this class to FriendslistAdapter. The problem is with the view below,
    //because I need to invoke different one.
    //Adapters call the getView() method which returns a view for each item within the adapter view.
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.shared_playlist_friends_custom_v, null);
        TextView username = (TextView) view.findViewById(R.id.friends_shared_playlist_custom_view_username);

        username.setText(userList.get(position).getUserName());
        return view;
    }
}