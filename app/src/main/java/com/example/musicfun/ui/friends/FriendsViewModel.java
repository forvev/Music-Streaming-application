package com.example.musicfun.ui.friends;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.User;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<User>> m_userNames = new MutableLiveData<>();
    private MutableLiveData<ArrayList<User>> m_searchUserResult = new MutableLiveData<>();
    private ArrayList<User> userArrayList;
    private ArrayList<User> searchUserResult;
    Database db;
    Application application;
    SharedPreferences sp;
    String token;

    public FriendsViewModel(Application application) {
        super(application);
        this.application = application;
        db = new Database(application.getApplicationContext());
        userArrayList = new ArrayList<>();
        searchUserResult = new ArrayList<>();
        m_userNames.setValue(userArrayList);
        m_searchUserResult.setValue(searchUserResult);
        sp = getApplication().getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
        token = sp.getString("token", "");
    }

    public void init() {
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for (int i = 0; i < userNames1.length(); i++) {
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getJSONObject(i).getString("username"), userNames1.getJSONObject(i).getString("_id"));
                        userArrayList.add(user);
                    }
                    m_userNames.setValue(userArrayList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        }, "user/allFriends?auth_token=" + token);
    }

    public void initSearch() {
        searchUserResult.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("Users");
                    //Log.d("onSucces", userNames1.getString(0));
                    for (int i = 0; i < userNames1.length(); i++) {
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getString(i));
                        searchUserResult.add(user);
                    }
                    m_searchUserResult.setValue(searchUserResult);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        }, "get/allUsers?auth_token=" + token);
    }

    public void filter(String name, String token) {
        searchUserResult.clear();
        db.searchUser(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("Users");
                    for (int i = 0; i < userNames1.length(); i++) {
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getString(i));
                        searchUserResult.add(user);
                    }
                    m_searchUserResult.setValue(searchUserResult);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        }, name, token);
    }

    public void sendMsgWithBodyDelete(String url, int i) {
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for (int i = 0; i < userNames1.length(); i++) {
                        User user = new User(userNames1.getJSONObject(i).getString("username"));
                        userArrayList.add(user);
                    }
                    String toDelete = userArrayList.get(i).getUserName();
                    db.addMsg(new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            userArrayList.remove(i);
                            m_userNames.setValue(userArrayList);
                        }

                        @Override
                        public void onError(VolleyError error) {
                        }
                    }, url, toDelete);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        }, "user/allFriends?auth_token=" + token);
    }

    public void sendMsgWithBodyAdd(String userToBeAdded) {
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for (int i = 0; i < userNames1.length(); i++) {
                        User user = new User(userNames1.getJSONObject(i).getString("username"));
                        userArrayList.add(user);
                    }
                    db.addMsg(new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            userArrayList.add(new User(userToBeAdded));
                            m_userNames.setValue(userArrayList);
                        }

                        @Override
                        public void onError(VolleyError error) {
                        }
                    }, "user/addFriend?auth_token=" + token, userToBeAdded);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
            }
        }, "user/allFriends?auth_token=" + token);

    }

    public MutableLiveData<ArrayList<User>> getUserNames() {
        return m_userNames;
    }

    public MutableLiveData<ArrayList<User>> getM_searchUserResult() {
        return m_searchUserResult;
    }

    public void add_user_to_shared_playlist(String url, String user_id, String playlist_id) {
        db.add_friends_to_playlist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url, user_id, playlist_id);
    }

    public void delete_user_from_shared_playlist(String url, String user_id, String playlist_id, int position) {
        db.add_friends_to_playlist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                userArrayList.remove(position);
                m_userNames.setValue(userArrayList);
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url, user_id, playlist_id);
    }

    public void fetch_shared_friends(String url, String playlist_id) {
        userArrayList.clear();
        db.all_friends_to_shared_playlist(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for (int i = 0; i < userNames1.length(); i++) {
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getJSONObject(i).getString("username"), userNames1.getJSONObject(i).getString("_id"));
                        userArrayList.add(user);

                    }
                    m_userNames.setValue(userArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url, playlist_id);
    }
}