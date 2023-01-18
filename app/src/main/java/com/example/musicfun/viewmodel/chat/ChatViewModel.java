package com.example.musicfun.viewmodel.chat;

import android.app.Application;
import android.content.SharedPreferences;
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
import java.util.Calendar;
import java.util.Date;

public class ChatViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Message>> messagesList = new MutableLiveData<>();
    private ArrayList<Message> messageArrayList;

    private MutableLiveData<ArrayList<String>> badWordsList = new MutableLiveData<>();
    private ArrayList<String> badWords;
    Application application;
    Database db;


    public ChatViewModel(@NonNull Application application) {
        super(application);
        messagesList = new MutableLiveData<>();
        this.application = application;
        messageArrayList =  new ArrayList<>();
        messagesList.setValue(messageArrayList);

        badWordsList = new MutableLiveData<>();
        badWords = new ArrayList<>();
        badWordsList.setValue(badWords);

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

    public void sendMsg(String token, String partner, String message, String ownName){
        db.sendChatMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    Message msg = new Message(message, "0", result.getString("time"), ownName);
                    messageArrayList.add(msg);
                    messagesList.setValue(messageArrayList);
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token, partner, message);
    }

    public void getBadWords(String token){
        badWords.clear();
        db.getBadWords(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                try{
                    JSONArray words = (JSONArray) result.get("badwords");
                    //Log.d("badWordsExtra", words.length() + "");
                    for(int i=0; i<words.length(); i++){
                       badWords.add(words.getString(i));
                    }
                    badWordsList.setValue(badWords);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, token);
    }

    public MutableLiveData<ArrayList<Message>> getMessages() {return messagesList;}

    public void activeAdd(Message msg){
        messageArrayList.add(msg);
        messagesList.postValue(messageArrayList);
    }

    public MutableLiveData<ArrayList<String>> getBadWordsList(){
        return badWordsList;
    }

}
