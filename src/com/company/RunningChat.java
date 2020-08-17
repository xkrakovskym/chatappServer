package com.company;

import java.io.IOException;

public class RunningChat implements Runnable {

    private Thread t;
    private final String threadName;
    private Client client;
    private Server server;

    public RunningChat(String threadName, Server server, Client client) {
        this.server = server;
        this.client = client;
        this.threadName = threadName;
    }

    public void start(){
        t = new Thread(this, threadName);
        t.start();
    }

    @Override
    public void run() {
            try {
                while (true) {
                    Message message = client.readMessage();
                    server.transferMessage(message);
                }
            } catch (IOException | ClassNotFoundException e ) {
                // close socket remove client from map
                //e.printStackTrace();
                server.disconnectClient(client.username);
            }
    }


}
