package com.company;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private Map<String, Client> clients;
    private ServerSocket serverSocket;
    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
        clients = new ConcurrentHashMap<>();
    }

    public void startServer() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            Client newClient = new Client(clientSocket);
            newClient.login();
            clients.put(newClient.username, newClient);

            RunningChat newChat = new RunningChat(newClient.username + "_chatThread", this, newClient);
            newChat.start();
        }
    }

    public void transferMessage(Message message) {
        try {
            if (clients.containsKey(message.getRecipient())) {
                Client recipient = clients.get(message.getRecipient());
                recipient.writeMessage(message);
            } else {
                Client recipient = clients.get(message.getSender());
                recipient.writeMessage(
                        new Message("server",
                                message.getSender(),
                                String.format("Can't reach %s, the contact is offline at the moment", message.getRecipient())));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectClient(String username) {
        try {
            Client disconnectingClient = clients.get(username);
            disconnectingClient.closeSocket();
            clients.remove(username);
            System.out.println(String.format("%s have been disconnected", username));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
