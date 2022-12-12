package com.example.musicfun.adapter;

import static android.content.Context.MODE_PRIVATE;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import com.example.musicfun.R;
import com.example.musicfun.databinding.FragmentFriendsBinding;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.FriendFragmentInterface;
import com.example.musicfun.repository.Database;
import com.example.musicfun.ui.friends.FriendsViewModel;
import com.example.musicfun.ui.friends.Friends_DBAccess;

import java.util.ArrayList;
import java.util.List;

public class FriendsListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<User> userList = null;
    private ArrayList<User> arrayList;
    Friends_DBAccess dbAccess;
    SharedPreferences sp;
    FriendFragmentInterface fi;


    public FriendsListAdapter(Context context, List<User> userList, FriendFragmentInterface fi){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.friends_custom_view, null);
        TextView username = (TextView) view.findViewById(R.id.friends_custom_view_username);
        ImageView profile = (ImageView) view.findViewById(R.id.friends_custom_view_profile);
        ImageView delete = (ImageView) view.findViewById(R.id.friends_custom_view_delete);

        RelativeLayout clickArea = (RelativeLayout) view.findViewById(R.id.user_and_id);
/*
        clickArea.setOnClickListener(click -> startChat(i));
        profile.setOnClickListener(click -> getProfile(i));  */
        delete.setOnClickListener(click -> fi.deleteFriend(i));

        username.setText(userList.get(i).getUserName());

        return view;
    }

/*
    private void deleteFriend(int i) {
        sp = inflater.getContext().getSharedPreferences("login", MODE_PRIVATE);
        //Log.d("thisIsWhy", dbAccess.sendMsgWithBody());
        Toast.makeText(inflater.getContext(),"Deleted ",Toast.LENGTH_SHORT).show();

        dbAccess.sendMsgWithBody("user/deleteFriend?auth_token=" + sp.getString("token", ""), userList.get(i).getUserName());
    }

    private void getProfile(int i) {
    }

    private void startChat(int i) {
        Toast.makeText(inflater.getContext(), "Here we go to chat", Toast.LENGTH_SHORT).show();
    }
*/

}
