package com.example.musicfun;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.interfaces.ServerCallBack;
import com.example.musicfun.search.Songs;

import org.json.JSONObject;

import java.util.ArrayList;

public class Database {

    private String baseUrl = "http://10.0.2.2:3000/";
    private ArrayList<Songs> songsArrayList = new ArrayList<>();
    Context context;

//    private MutableLiveData<ArrayList<Songs>> myLiveData = new MutableLiveData<>();

    public Database(Context context){
        this.context = context;
    }

//    public ArrayList<Songs> sendMsg(String query, Context context) throws JSONException {
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                // TODO: switch case
//                try {
//                    JSONArray songTitles = (JSONArray) response.get("All songs");
//                    for (int i = 0; i < songTitles.length(); i++) {
//                        Songs s = new Songs(songTitles.getJSONObject(i).getString("title"), songTitles.getJSONObject(i).getString("artist"), songTitles.getJSONObject(i).getInt("id"));
//                        songsArrayList.add(s);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("Error Response" + error.getMessage());
//            }
//        });
//        requestQueue.add(request);
//        return songsArrayList;
//    }

    public void sendMsg(ServerCallBack callback, String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,baseUrl + url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
                // TODO: switch case
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Response" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }
}
