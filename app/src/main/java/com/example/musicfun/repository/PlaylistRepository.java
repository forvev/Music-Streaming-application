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
 * This class handles most server accesses related to playlists.
 * The exceptions are add_friends_to_playlist and all_friends_to_shared_playlist in the database class.
 */
public class PlaylistRepository {
    private String getDefaultPlaylist = "http://10.0.2.2:3000/user/getDefaultPlaylist?auth_token=";
    private String getAllPlaylists = "http://10.0.2.2:3000/playlist/getUsersPlaylists?auth_token=";
    private String setAsDefault = "http://10.0.2.2:3000/user/setDefaultPlaylist?auth_token=";
    private String renamePlaylist = "http://10.0.2.2:3000/playlist/changePlaylistName?auth_token=";
    private String deletePlaylist = "http://10.0.2.2:3000/playlist/deletePlaylist?auth_token=";
    private String searchPlaylistByName = "http://10.0.2.2:3000/playlist/getUsersPlaylistsByName?auth_token=";
    private String getSongsFromPlaylist = "http://10.0.2.2:3000/playlist/getSongsFromPlaylist?auth_token=";
    private String deleteSongsFromPlaylist = "http://10.0.2.2:3000/playlist/removeSong?auth_token=";
    private String addSongToPlaylist = "http://10.0.2.2:3000/playlist/addSong?auth_token=";
    private String searchSongListByName = "http://10.0.2.2:3000/playlist/getSongsFromPlaylistByName?auth_token=";
    private String createPlaylist = "http://10.0.2.2:3000/playlist/createPlaylist?auth_token=";
    private String getAllOwnedPlaylists = "http://10.0.2.2:3000/playlist/getAllUsersOwnedPlaylists?auth_token=";

//      shared playlists relevant urls
     private String getSharedPlaylists = "http://10.0.2.2:3000/playlist/getUsersSharedPlaylists?auth_token=";
     private String createSharedPlaylist = "http://10.0.2.2:3000/playlist/createSharedPlaylist?auth_token=";
     private String setAsShare = "http://10.0.2.2:3000/playlist/convertPlaylistToSharedPlaylist?auth_token=";
    Context context;

    public PlaylistRepository(Context context){
        this.context = context;
    }

    public void setAsShare(ServerCallBack callback, String token, String playlist_id){
        JSONObject shared_playlist = new JSONObject();
        try {
            shared_playlist.put("playlist_id", playlist_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, setAsShare + token, shared_playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error setAsShare" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }


    public void getDefaultPlaylist(ServerCallBack callback, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getDefaultPlaylist + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getDefaultPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void getAllPlaylists(ServerCallBack callback, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getAllPlaylists + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getAllPlaylists" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void getAllOwnedPlaylists(ServerCallBack callback, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getAllOwnedPlaylists + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getAllPlaylists" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void getSharedPlaylist(ServerCallBack callback, String token){
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getSharedPlaylists + token, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getSharedPlaylist " + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void setAsDefault(ServerCallBack callback, String playlist_id, String token){
        JSONObject default_playlist = new JSONObject();
        try {
            default_playlist.put("new_default_id", playlist_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, setAsDefault + token, default_playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error setAsDefault" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void renamePlaylist(ServerCallBack callback, String playlist_id, String new_name, String token){
        JSONObject playlist = new JSONObject();
        try {
            playlist.put("playlist_id", playlist_id);
            playlist.put("new_name", new_name);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, renamePlaylist + token, playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public void deletePlaylist(ServerCallBack callback, String playlist_id, String token){
        JSONObject playlist = new JSONObject();
        try {
            playlist.put("playlist_id", playlist_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, deletePlaylist + token, playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public void searchPlaylistByName(ServerCallBack callback, String letter, String token){
        JSONObject typedLetter = new JSONObject();
        try {
            typedLetter.put("pattern", letter);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, searchPlaylistByName + token, typedLetter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error setGetDefaultPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void getSongsFromPlaylist(ServerCallBack callback, String playlist_id, String token){
        JSONObject playlist = new JSONObject();
        try {
            playlist.put("playlist_id", playlist_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, getSongsFromPlaylist + token, playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error getSongsFromPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void deleteSongsFromPlaylist(ServerCallBack callback, String playlist_id, String song_id, String token){
        JSONObject to_delete = new JSONObject();
        try {
            to_delete.put("playlist_id", playlist_id);
            to_delete.put("song_id", song_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, deleteSongsFromPlaylist + token, to_delete, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error deleteSongsFromPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void addSongToPlaylist(ServerCallBack callback, String playlist_id, String song_id, String token){
        JSONObject to_add = new JSONObject();
        try {
            to_add.put("playlist_id", playlist_id);
            to_add.put("song_id", song_id);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, addSongToPlaylist + token, to_add, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        });
        requestQueue.add(request);
    }

    public void searchSongListByName(ServerCallBack callback, String playlistId, String letter, String token){
        JSONObject typedLetter = new JSONObject();
        try {
            typedLetter.put("playlist_id", playlistId);
            typedLetter.put("pattern", letter);
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, searchSongListByName + token, typedLetter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error setGetDefaultPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void createPlaylist(ServerCallBack callback, String name, String token){
        JSONObject create_playlist = new JSONObject();
        try {
            if(name.isEmpty()){
                create_playlist = null;
            }
            else{
                create_playlist.put("playlist_name", name);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createPlaylist + token, create_playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error createPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

    public void createSharedPlaylist(ServerCallBack callback, String name, String token){
        JSONObject create_playlist = new JSONObject();
        try {
            if(name.isEmpty()){
                create_playlist = null;
            }
            else{
                create_playlist.put("playlist_name", name);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, createSharedPlaylist + token, create_playlist, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error createPlaylist" + error.getMessage());
            }
        });
        requestQueue.add(request);
    }

}
