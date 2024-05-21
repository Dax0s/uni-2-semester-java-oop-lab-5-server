package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<Client> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            return;
        }

        int serverPort = Integer.parseInt(args[0]);

        ServerSocket serverSocket = new ServerSocket(serverPort);
        System.out.println("Server started. Waiting for clients...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            Client client = new Client(clientSocket, clients);
            clients.add(client);
            new Thread(client).start();
        }
    }
}