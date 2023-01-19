package com.example.musicfun.interfaces;

public interface FriendFragmentInterface {

    void deleteFriend(int i, String partner);
    void deleteFirend(int position, String user_id, String playlist_id);
    void addFriend(String name, int i);
    void getProfile(int i);
    void startChat(String name);
}
