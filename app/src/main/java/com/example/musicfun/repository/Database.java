package com.example.musicfun.repository;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.datatype.Songs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Database {

    private String baseUrl = "http://10.0.2.2:3000/";
    private String searchUrl = "http://10.0.2.2:3000/get/titleStartsWith?string=";
    private String searchUserUrl = "http://10.0.2.2:3000/get/userStartsWith?auth_token=";
    private String getChatUrl = "http://10.0.2.2:3000/get/chat?auth_token=";
    private String sendChatMsgUrl = "http://10.0.2.2:3000/get/storeMessage?auth_token=";
    private ArrayList<Songs> songsArrayList = new ArrayList<>();
    Context context;

    public Database(Context context){
        this.context = context;
    }

    public void sendMsg(ServerCallBack callback, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,baseUrl + url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Response 1" + error.getMessage() + "url = " + baseUrl + url);
            }
        });
        requestQueue.add(request);
    }
//TODO: Do we need a response from the server and if so what shall we do with it?
    public void addMsg(ServerCallBack callBack, String url, String username){
        JSONObject user = new JSONObject();
        try {
            user.put("friendName", username);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("onSuccess", username);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, baseUrl + url, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("disTest", "Hello");
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        });
        requestQueue.add(request);

    }

    public void getChat(ServerCallBack callBack, String token, String chatpartner) {
        JSONObject user = new JSONObject();
        try {
            user.put("chatPartner", chatpartner);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        //Log.d("disTest", "is ok");
        //TODO: Warten das Dominik aus der Antwort ein JSONObject macht und dann weiter behandeln
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, getChatUrl + token, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("disTest", "now its ok");
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("disTest", "Fehlgeschlagen" + error.getMessage());
                callBack.onError(error);
            }
        });
        requestQueue.add(request);

    }

    public void sendChatMsg(ServerCallBack callBack, String token, String partner, String message){
        //TODO: store messages with neutral warning like this
        String theMessage = message;
        if (message.equals("Your chat partner used a bad word.")){
            theMessage = "Message censored - Bad word used";
        };
        JSONObject msg = new JSONObject();
        try {
            msg.put("chatPartner", partner);
            msg.put("message", theMessage);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.d("onSuccess", message);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, sendChatMsgUrl + token, msg, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.d("disTest", "Hello");
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("disTest", "Fehlgeschlagen" + error.getMessage());
                callBack.onError(error);
            }
        });
        requestQueue.add(request);

    }



    public void search(ServerCallBack callback, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,searchUrl + url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Response" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void searchUser(ServerCallBack callback, String url, String token) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,searchUserUrl + token + "&string=" + url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Response" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }




    public void test(){
        Log.d("connecGood?", "Connected");
    }
}