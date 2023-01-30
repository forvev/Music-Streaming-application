package com.example.musicfun.datatype;

public class User {

    //private String login;
    //private String password;
    private String user_id;
    private String userName;
    private boolean accepted;
    private boolean requestSend;
    //private String userPicture; //TODO: ensure about the type of images

    public User(String userName){
        this.userName = userName;
    }



    public User(String userName, String user_id, Boolean accepted, Boolean requestSend){
        this.userName = userName;
        this.user_id = user_id;
        this.accepted = accepted;
        this.requestSend = requestSend;
    }

    public String getUserName() {
        return userName;
    }

    public String getUser_id() {return user_id;}

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isRequestSend() {
        return requestSend;
    }
}
