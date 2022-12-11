package com.example.musicfun.ui.friends;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.repository.Database;

import org.json.JSONException;
import org.json.JSONObject;

public class Friends_DBAccess {

    Context context;
    Database db;

    public Friends_DBAccess(Context context){
        this.context = context;
        db = new Database(context);
    }

    public void sendMsgWithBody(String url, String user){
        //db.test();
        //Log.d("onSuccess", url + " " + user);
        db.addMsg(new ServerCallBack() {
            @Override
            public void onSuccess(JSONObject result) {

                try{
                    String answer = result.getString("message");
                    Log.d("onSuccess", answer);
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
