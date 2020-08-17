package com.company;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public String username;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void login() {
        try {
            username = (String) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("User with username:%s was connected", username));
    }

    public Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) inputStream.readObject();
    }

    public void writeMessage(Message message) throws IOException {
        outputStream.writeObject(message);
    }

    public void closeSocket() throws IOException {
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
