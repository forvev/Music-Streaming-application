package com.example.musicfun.viewmodel.chat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Message;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.Database;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Message>> messagesList = new MutableLiveData<>();
    private ArrayList<Message> messageArrayList;
    Application application;
    Database db;


    public ChatViewModel(@NonNull Application application) {
        super(application);

        this.application = application;
        messageArrayList =  new ArrayList<>();
        messagesList.setValue(messageArrayList);
        db = new Database(application.getApplicationContext());
    }

    //display the initial list of messages
    public void init(String url){
        messageArrayList.clear();
        db.sendMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result){
                try{
                    JSONArray listOfMessages = (JSONArray) result.get("messages");
                    for (int i=0; i<listOfMessages.length(); i++){
                        //TODO: check what's the name in the JSON object
                        Message message = new Message(listOfMessages.getJSONObject(i).getString("message"));
                        messageArrayList.add(message);
                    }
                    messagesList.setValue(messageArrayList);
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
