package com.example.musicfun.ui.friends;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

    public void filter(String url){
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

    public MutableLiveData<ArrayList<User>> getUserNames() {return userNames;}
}