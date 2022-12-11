package com.example.musicfun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicfun.datatype.User;
import com.example.musicfun.R;

import java.util.ArrayList;
import java.util.List;

public class SearchUserResultAdapter extends BaseAdapter {
    Context mContext;
    private List<User> userList = null;
    LayoutInflater inflater;
    private ArrayList<User> arrayList;

    public SearchUserResultAdapter(Context context, List<User> userList){
        mContext = context;
        this.userList = userList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(userList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() { return userList.size();}

    @Override
    public Object getItem(int position) {return userList.get(position);}

    @Override
    public long getItemId(int position) { return position;}

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.user_search_result_lv, null);
            holder.name = (TextView) view.findViewById(R.id.user_name);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        // set the results into TextViews
        holder.name.setText(userList.get(position).getUserName());

        return view;
    }
}
