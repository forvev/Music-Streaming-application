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

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private final String url_login = "http://10.0.2.2:3000/account/login";
    private final String url_register = "http://10.0.2.2:3000/account/signup";
    private final String url_reset = "http://10.0.2.2:3000/account/change-password?auth_token=";
    private final String url_saveListenStatus = "http://10.0.2.2:3000/user/saveData?auth_token=";
    private final String url_getListenStatus = "http://10.0.2.2:3000/user/getData?auth_token=";

    private Context context;

    public LoginRepository (Context context) {
        this.context = context;
    }

    public void login(String username, String password, ServerCallBack callBack){
        JSONObject user = new JSONObject();
        try {
            user.put("username", username);
            user.put("password", password);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url_login, user, new Response.Listener<JSONObject>() {
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

    public void register(String username, String password, ServerCallBack callBack) {
        JSONObject user = new JSONObject();
        try {
            user.put("username", username);
            user.put("password", password);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url_register, user, new Response.Listener<JSONObject>() {
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

    public void reset(String password, String n_password, String token, ServerCallBack callBack) {
        JSONObject user = new JSONObject();
        try {
            user.put("old_password", password);
            user.put("new_password", n_password);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url_with_token = url_reset + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url_with_token, user, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public void saveDataWhenLogout(int startItemIndex, String startPosition, String savedPlaylist, String token){
        JSONObject listenStatus = new JSONObject();
        try {
            listenStatus.put("startItemIndex", startItemIndex);
            listenStatus.put("startPosition", startPosition);
            listenStatus.put("savedPlaylist", savedPlaylist);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url_saveListenStatus + token, listenStatus, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("save data when logout did not work! " + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void getDataWhenLogin(String token, ServerCallBack callBack) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url_getListenStatus + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callBack.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callBack.onError(error);
            }
        });
        requestQueue.add(request);
    }
}