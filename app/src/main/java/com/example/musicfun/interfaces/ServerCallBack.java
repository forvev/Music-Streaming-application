package com.example.musicfun.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

public interface ServerCallBack {
    void onSuccess(JSONObject result);
    void onError(VolleyError error);
}
