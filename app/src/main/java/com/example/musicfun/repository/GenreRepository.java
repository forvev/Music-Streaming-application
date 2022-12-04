package com.example.musicfun.repository;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicfun.interfaces.ServerCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class GenreRepository {
    private String url_receive_genre = "http://10.0.2.2:3000/getallsongs";
    private String url_post_genre = "http://10.0.2.2:3000/getallsongs";

    private Context context;

    public GenreRepository(Context context) {
        this.context = context;
    }

    public void getAllGenres(ServerCallBack callBack) throws JSONException {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_receive_genre, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Response from receive_genre" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void sendGenres(ServerCallBack callBack){



        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url_receive_genre, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error Response from receive_genre" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }


}
