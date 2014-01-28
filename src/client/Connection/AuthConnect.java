package client.Connection;

import client.GUI.ConnectView;

import java.io.IOException;
import java.net.Socket;

public class AuthConnect extends Thread{
    private ConnectView connectView;
    private String serverIP;
    private int serverPort;

    public AuthConnect(ConnectView connectView, String serverIP, int serverPort) {
        this.connectView = connectView;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void run() {
        Socket clientSocket;
        try {
            clientSocket = new Socket(serverIP, serverPort);
            connectView.setupAuthentication(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}