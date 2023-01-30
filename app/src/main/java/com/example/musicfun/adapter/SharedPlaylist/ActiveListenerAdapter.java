package com.example.musicfun.adapter.SharedPlaylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicfun.R;

import java.util.List;

/**
 * Adapter for listing users who are now active in a room
 */
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
        view = inflater.inflate(R.layout.row_username, null);
        TextView username = (TextView) view.findViewById(R.id.user_name);
        username.setText(userList.get(i));
        return view;
    }
}
