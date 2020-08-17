package com.company;


import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Server myServer = new Server(9999);
            myServer.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
