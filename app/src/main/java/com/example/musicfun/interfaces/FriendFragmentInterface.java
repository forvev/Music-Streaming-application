package com.example.musicfun.interfaces;

import android.view.View;

import java.util.ArrayList;

public interface FriendFragmentInterface {

    void deleteFriend(int i, String partner);
    void deleteFriend(int position, String user_id, String playlist_id);
    void addFriend(String name, int i);
    void getProfile(int i);
    void startChat(String name);
    void add_friends(ArrayList<String> arrayList);
}
