package com.example.musicfun.viewmodel.chat;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.VolleyError;
import com.example.musicfun.datatype.Message;
import com.example.musicfun.datatype.User;
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
        messagesList = new MutableLiveData<>();
        this.application = application;
        messageArrayList =  new ArrayList<>();
        messagesList.setValue(messageArrayList);
        db = new Database(application.getApplicationContext());
    }

    //display the initial list of messages
    public void init(String token, String chatpartner){
        messageArrayList.clear();
        //Log.d("disTest", "good so far");
        db.getChat(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                //Log.d("disTest", "good so far");
                try{
                //TODO: Wenn request funktioniert hier das Array bearbeiten
                    JSONArray messages = (JSONArray) result.get("messages");
                    //Log.d("disTest", messages.get(0).toString());
                    //Log.d("disTest", messages.getJSONObject(0).getString("sender"));
                    for(int i=0; i<messages.length(); i++){
                        JSONObject mess = messages.getJSONObject(i);
                        Message message = new Message(mess.getString("message"),mess.getString("date"), mess.getString("time"), mess.getString("sender"));
                        messageArrayList.add(message);
                    }
                    messagesList.setValue(messageArrayList);

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token, chatpartner);

    }

    public MutableLiveData<ArrayList<Message>> getMessages() {return messagesList;}

}
