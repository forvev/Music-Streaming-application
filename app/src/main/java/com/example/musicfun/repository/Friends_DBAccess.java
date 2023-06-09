package com.example.musicfun.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.Database;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class was intended for all extra database accesses, as have to do with the friend system.
 * However, these accesses were then implemented in the database class, so that this has no more use.
 */
public class Friends_DBAccess {

    Context context;
    Database db;

    public Friends_DBAccess(Context context){
        this.context = context;
        db = new Database(context);
    }

    public void sendMsgWithBody(String url, String user){
        db.addMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try{
                    String answer = result.getString("message");
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {

            }
        }, url, user);
    }
}
