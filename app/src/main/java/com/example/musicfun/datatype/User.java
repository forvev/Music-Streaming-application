package com.example.musicfun.datatype;

public class User {

    //private String login;
    //private String password;
    private String id;
    private String userName;
    //private String userPicture; //TODO: ensure about the type of images

    public User(String id, String userName){
        this.id = id;
        this.userName = userName;
    }
}
