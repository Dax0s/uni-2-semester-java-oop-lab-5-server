package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class Client implements Runnable {
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final List<Client> clients;

    public Client(Socket socket, List<Client> clients) {
        this.clientSocket = socket;
        this.clients = clients;

        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            String username = getUsername();
            sendMessage("User " + username + " connected.", false);

            out.println("Welcome to the chat, " + username + "!");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                sendMessage("[" + username + "]: " + inputLine, true);
            }

            clients.remove(this);

            sendMessage("User [" + username + "]" + " disconnected", true);

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getUsername() throws IOException {
        out.println("Enter your username: ");
        return in.readLine();
    }

    private void sendMessage(String message, boolean sendToCurrentUser) {
        System.out.println(message);

        for (Client client : clients) {
            if (client != this || sendToCurrentUser)
                client.out.println(message);
        }
    }
}
