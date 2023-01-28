package com.example.musicfun.adapter.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicfun.datatype.User;
import com.example.musicfun.R;
import com.example.musicfun.interfaces.CloseSearchViewInterface;
import com.example.musicfun.ui.friends.FriendsViewModel;

import java.util.ArrayList;
import java.util.List;

public class SearchUserResultAdapter extends BaseAdapter {
    Context mContext;
    private List<User> userList = null;
    LayoutInflater inflater;
    private ArrayList<User> arrayList;
    FriendsViewModel friendsViewModel;
    CloseSearchViewInterface closeSearchViewInterface;

    public SearchUserResultAdapter(Context context, List<User> userList, FriendsViewModel friendsViewModel, CloseSearchViewInterface closeSearchViewInterface){
        mContext = context;
        this.userList = userList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(userList);
        this.friendsViewModel = friendsViewModel;
        this.closeSearchViewInterface = closeSearchViewInterface;
    }

    public class ViewHolder {
        TextView name;
        LinearLayout linearLayout;
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
            view = inflater.inflate(R.layout.row_username, null);
            holder.name = (TextView) view.findViewById(R.id.user_name);
            holder.linearLayout = (LinearLayout) view.findViewById(R.id.ll_search_user);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        // set the results into TextViews
        holder.name.setText(userList.get(position).getUserName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User u = userList.get(position);
                friendsViewModel.sendMsgWithBodyAdd(u.getUserName());
                closeSearchViewInterface.updateView(view);
            }
        });
        return view;
    }
}
