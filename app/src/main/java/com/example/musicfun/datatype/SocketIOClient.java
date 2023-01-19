package com.example.musicfun.datatype;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketIOClient {

    public Socket mSocket;

    {
        try{
            //ansonsten so ws://127.0.0.1:3001'
            mSocket = IO.socket("http://100.110.104.112:3001");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);

    }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
