package com.example.musicfun.viewmodel;

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

/**
 * This ViewModel class is used for data handling of all classes that make friend related server accesses.
 * This class represents an intermediate station before the actual database access in the database class.
 */
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
                        JSONObject userObject = userNames1.getJSONObject(i);
                        User user = new User(userObject.getString("username"), userObject.getString("_id"), userObject.getBoolean("accepted"), userObject.getBoolean("addedByMe"));
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
                        User user = new User(userNames1.getString(i));
                        boolean alreadyAdded = false;
                        for (int j = 0; j < searchUserResult.size(); j++) {
                            if (searchUserResult.get(j).getUserName().equals(user.getUserName())) {
                                alreadyAdded = true;
                            }
                        }
                        if (!alreadyAdded) {
                            searchUserResult.add(user);
                        }
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

    public void sendMsgWithBodyDelete(String url, int i, String name) {
        String delete = name;
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for (int i = 0; i < userNames1.length(); i++) {
                        JSONObject userObject = userNames1.getJSONObject(i);
                        User user = new User(userObject.getString("username"), userObject.getString("_id"), userObject.getBoolean("accepted"), userObject.getBoolean("addedByMe"));
                        userArrayList.add(user);
                    }
                    if(userArrayList.size() > i){
                        String toDelete = userArrayList.get(i).getUserName();
                        if(toDelete.equals(delete)){
                            db.addMsg(new ServerCallBack() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    //if needed, because when clicking to fast on same delete icon, IoB Exception would happen
                                    if(userArrayList.size() != 0){
                                        userArrayList.remove(i);
                                    }
                                    m_userNames.setValue(userArrayList);
                                }

                                @Override
                                public void onError(VolleyError error) {
                                }
                            }, url, toDelete);
                        }else{
                            m_userNames.setValue(userArrayList);
                        }
                    }else{
                        m_userNames.setValue(userArrayList);
                    }

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
                        JSONObject userObject = userNames1.getJSONObject(i);
                        User user = new User(userObject.getString("username"), userObject.getString("_id"), userObject.getBoolean("accepted"), userObject.getBoolean("addedByMe"));
                        userArrayList.add(user);
                    }
                    db.addMsg(new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            userArrayList.add(new User(userToBeAdded, "", false, true));
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

    public void sendMsgWithBodyAccept(String userToBeAdded, int i) {
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for (int i = 0; i < userNames1.length(); i++) {
                        JSONObject userObject = userNames1.getJSONObject(i);
                        User user = new User(userObject.getString("username"), userObject.getString("_id"), userObject.getBoolean("accepted"), userObject.getBoolean("addedByMe"));
                        userArrayList.add(user);
                    }
                    db.addMsg(new ServerCallBack() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            userArrayList.add(i, new User(userToBeAdded, "", true, false));
                            userArrayList.remove(i+1);
                            m_userNames.setValue(userArrayList);
                        }

                        @Override
                        public void onError(VolleyError error) {
                        }
                    }, "user/acceptFriend?auth_token=" + token, userToBeAdded);

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
                        JSONObject u = userNames1.getJSONObject(i);
                        User user = new User(u.getString("username"), u.getString("_id"), u.getBoolean("accepted"), u.getBoolean("addedByMe"));
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