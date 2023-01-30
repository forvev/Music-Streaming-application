package com.example.musicfun.datatype;

public class Message {
    String message;
    String sender;
    String date;
    String time;

    public Message(String message, String date, String time, String sender){
        this.message = message;
        this.date = date;
        this.time = time;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
