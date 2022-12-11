package com.example.musicfun.ui.friends;

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

    private MutableLiveData<ArrayList<User>> userNames = new MutableLiveData<>();
    private ArrayList<User> userArrayList;
    Database db;
    Application application;
    SharedPreferences sp;
    private String serverAnswer;

    String user;

    public FriendsViewModel(Application application) {
        super(application);
        userNames = new MutableLiveData<>();
        this.application = application;
        db = new Database(application.getApplicationContext());
        userArrayList = new ArrayList<>();
        userNames.setValue(userArrayList);
    }

    public void init(String url){
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("friends");
                    for(int i=0; i< userNames1.length(); i++){
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getJSONObject(i).getString("username"));
                        userArrayList.add(user);

                    }
                    userNames.setValue(userArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
            }

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url);
    }

    public void initSearch(String url){
        userArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("Users");
                    //Log.d("onSucces", userNames1.getString(0));
                    for(int i=0; i< userNames1.length(); i++){
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getString(i));
                        userArrayList.add(user);

                    }
                    userNames.setValue(userArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url);
    }


    public void filter(String url){
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames1 = (JSONArray) result.get("Users");
                    for(int i=0; i< userNames1.length(); i++){
                        //TODO: ask server side about the names
                        User user = new User(userNames1.getJSONObject(i).getString("username"));
                        userArrayList.add(user);

                    }
                    userNames.setValue(userArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url);
    }

    public void sendMsgWithBodyDelete(String url, int i){
        //db.test();
        //Log.d("onSuccess", url + " " + user);

        db.addMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try{
                    String answer = result.getString("message");
                    Log.d("onSuccess", answer);
                    userArrayList.remove(i);
                    userNames.setValue(userArrayList);

                    //maybe thinnk on how to automatically add user
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url, userArrayList.get(i).getUserName());
    }

    public void sendMsgWithBodyAdd(String url, String userToBeAdded){
        //db.test();
        //Log.d("onSuccess", url + " " + user);

        db.addMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try{
                    String answer = result.getString("message");
                    Log.d("onSuccess", answer);

                    //maybe thinnk on how to automatically add user
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url, userToBeAdded);
    }

    public MutableLiveData<ArrayList<User>> getUserNames() {return userNames;}
}