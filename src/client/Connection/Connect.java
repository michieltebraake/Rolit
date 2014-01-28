package client.Connection;

import client.GUI.RolitView;

import java.io.IOException;
import java.net.Socket;

public class Connect extends Thread {
    private RolitView rolitView;
    private String serverIP;
    private int serverPort;

    public Connect(RolitView rolitView, String serverIP, int serverPort) {
        this.rolitView = rolitView;
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public void run() {
        Socket clientSocket;
        try {
            clientSocket = new Socket(serverIP, serverPort);
            rolitView.setupProtocol(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}