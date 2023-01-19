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

    private String baseUrl = "http://100.110.104.112:3000/";
    private String searchUrl = "http://100.110.104.112:3000/get/titleStartsWith?string=";
    private String searchUserUrl = "http://100.110.104.112:3000/get/userStartsWith?auth_token=";
    private String urlListenHistory = "http://100.110.104.112:3000/account/addListenHistory?auth_token=";
    private String fetchlyrics = "http://100.110.104.112:3000/get/lyrics";
    private String getChatUrl = "http://100.110.104.112:3000/get/chat?auth_token=";
    private String sendChatMsgUrl = "http://100.110.104.112:3000/get/storeMessage?auth_token=";
    private String addUsrToSharedPlaylist = "http://100.110.104.112:3000/get/storeMessage?auth_token=";
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
        JSONObject msg = new JSONObject();
        try {
            msg.put("chatPartner", partner);
            msg.put("message", message);
        }catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void sendListenHistory (String currentSongID, String token){
        JSONObject heardSong = new JSONObject();
        try {
            heardSong.put("songID", currentSongID);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlListenHistory + token, heardSong, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error when send ListenHistory to databank");
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

    public void fetchLyrics(ServerCallBack callback, String song_id){
        JSONObject song = new JSONObject();
        try {
            song.put("songID", song_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, fetchlyrics, song, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error fetchLyrics " + error.getMessage());
            }
        });
        requestQueue.add(request);

    }

    public void add_friends_to_playlist(ServerCallBack serverCallBack, String url, String user_id, String playlist_id){
        JSONObject added_friend = new JSONObject();

        try {
            added_friend.put("user_id",user_id);
            added_friend.put("playlist_id", playlist_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl + url, added_friend, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverCallBack.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public void all_friends_to_shared_playlist(ServerCallBack serverCallBack, String url, String playlist_id){
        JSONObject user_search = new JSONObject();

        try {
            user_search.put("playlist_id",playlist_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("play_list_id", String.valueOf(user_search));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl + url, user_search, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverCallBack.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public void get_users_from_shared_playlist(ServerCallBack serverCallBack, String url, String playlist_id){
        JSONObject user_search = new JSONObject();

        try {
            user_search.put("playlist_id",playlist_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("play_list_id", String.valueOf(user_search));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl + url, user_search, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverCallBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverCallBack.onError(error);
            }
        });
        requestQueue.add(request);
    }

}