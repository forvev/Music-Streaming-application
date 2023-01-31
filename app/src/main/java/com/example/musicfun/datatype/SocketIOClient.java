package com.example.musicfun.datatype;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;


public class SocketIOClient {

    public Socket mSocket;

    {
        try{
            //mSocket = IO.socket("https://10.0.2.2:3000");
            mSocket = IO.socket("https://100.110.104.112:3000");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);

    }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
