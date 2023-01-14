package com.example.musicfun.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicfun.R;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;
import com.example.musicfun.ui.friends.Friends_DBAccess;

import java.util.ArrayList;
import java.util.List;

public class ActiveListenerAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<String> userList = null;


    public ActiveListenerAdapter(Context context, List<String> activeUsersList){
        mContext = context;
        this.userList = activeUsersList;
        inflater = LayoutInflater.from(mContext);
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fragment_active_listener_item, null);
        TextView username = (TextView) view.findViewById(R.id.active_listeners_item_username);
        username.setText(userList.get(i));
        return view;
    }
}
