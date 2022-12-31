package com.example.musicfun.datatype;

public class Message {
    String message;
    User sender;
    long createdAt;

    public Message(String message){
        this.message = message;
    }
}
