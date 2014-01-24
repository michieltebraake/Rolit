package server;

import server.game.Mark;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
public class RolitServer {
    List<Socket> connections = new ArrayList<>();

    HashMap<Mark, Socket> authenticated = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("Starting server...");
        new RolitServer().start();
    }

    private void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(2727);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept();

                connections.add(clientSocket);

                System.out.println("Client connected!");

                ServerPeer peer = new ServerPeer(clientSocket);
                Thread thread = new Thread(peer);
                thread.start();

                if (connections.size() == 2) {

                }
                //peer.handleTerminalInput();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
