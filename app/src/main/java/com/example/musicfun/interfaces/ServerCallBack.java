package com.example.musicfun.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface ServerCallBack {
    void onSuccess(JSONObject result) throws JSONException;
    void onError(VolleyError error);
}
