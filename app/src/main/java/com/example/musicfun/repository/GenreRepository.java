package com.example.musicfun.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.interfaces.ServerCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is used to send changes to the user's genres to the database.
 */
public class GenreRepository {
    private String url_receive_genre = "";
    private String url_post_genre = "http://10.0.2.2:3000/account/sendgenres?auth_token=";

    private Context context;

    public GenreRepository(Context context) {
        this.context = context;
    }

//    public void getAllGenres(ServerCallBack callBack) throws JSONException {
//        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_receive_genre, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                callBack.onSuccess(response);
//            }
//        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                System.out.println("Error Response from receive_genre" + error.getMessage());
//            }
//        });
//        requestQueue.add(request);
//    }

    public void sendGenres(String token, ArrayList<String> selectedGenres, ServerCallBack callBack) throws JSONException {
        JSONArray arr = new JSONArray();
        Iterator iter = selectedGenres.iterator();
        while (iter.hasNext()) {
            arr.put(iter.next());
        }
        JSONObject toSend = new JSONObject();
        toSend.put("genres", arr);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url_post_genre + token, toSend, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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


}
