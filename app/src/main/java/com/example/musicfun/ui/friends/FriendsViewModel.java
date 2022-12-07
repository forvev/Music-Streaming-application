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

    private final MutableLiveData<String> mText;
    private ArrayList<User> userArrayList;
    Database db;

    public FriendsViewModel(Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is friends fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void init(String url){
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray userNames = (JSONArray) result.get("users");
                    for(int i=0; i< userNames.length(); i++){
                        //TODO: ask server side about the names
                        User user = new User(userNames.getJSONObject(i).getString("_id"), userNames.getJSONObject(i).getString("username"));
                        userArrayList.add(user);
                    }
                } catch (JSONException e) {
                e.printStackTrace();
            }

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url);
    }
}